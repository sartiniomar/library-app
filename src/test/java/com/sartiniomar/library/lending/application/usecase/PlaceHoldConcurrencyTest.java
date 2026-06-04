package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.application.port.out.PatronLendingRepository;
import com.sartiniomar.library.lending.application.support.InMemoryEventPublisher;
import com.sartiniomar.library.lending.domain.hold.BookAlreadyOnHoldException;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryBookInstanceRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryHoldRepository;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test de concurrencia para PlaceHoldService.
 * Usa solo clases de test / in-memory, no requiere Spring.
 */
class PlaceHoldConcurrencyTest {

  //@Test
  void shouldHandleConcurrency() throws Exception {

    // Repositorios in-memory (implementaciones de test)
    TestPatronLendingRepository patronRepo = new TestPatronLendingRepository();
    InMemoryBookInstanceRepository bookInstanceRepository = new InMemoryBookInstanceRepository();
    InMemoryHoldRepository holdRepository = new InMemoryHoldRepository();

    // Event publisher + domain service
    InMemoryEventPublisher publisher = new InMemoryEventPublisher();
    PlacingOnHoldService placingService = new PlacingOnHoldService();

    // Use case real construido con adaptadores in-memory
    PlaceHoldUseCase placeHoldUseCase = new PlaceHoldService(
        patronRepo,
        bookInstanceRepository,
        holdRepository,
        publisher,
        placingService
    );

    // Preparar datos
    BookInstance book = BookInstance.circulating(UUID.randomUUID());
    bookInstanceRepository.save(book);

    Patron patron = Patron.researcher();

    // Insertar patron solo en el test (helper de test)
    patronRepo.setupPatron(patron);

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch ready = new CountDownLatch(2);
    CountDownLatch start = new CountDownLatch(1);

    AtomicInteger success = new AtomicInteger();
    AtomicInteger failures = new AtomicInteger();

    Runnable task = () -> {
      ready.countDown();
      try {
        start.await();

        placeHoldUseCase.execute(new PlaceHoldCommand(patron.getId(), book.getId()));

        success.incrementAndGet();
      } catch (Exception e) {
        if (isConcurrencyRelated(e)) {
          failures.incrementAndGet();
        } else {
          throw new RuntimeException(e);
        }
      }
    };

    executor.submit(task);
    executor.submit(task);

    ready.await();
    start.countDown();

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    assertEquals(2, success.get() + failures.get());
    assertTrue(success.get() >= 1);
    assertTrue(failures.get() >= 1);
  }

  private boolean isConcurrencyRelated(Throwable e) {
    return e instanceof org.springframework.orm.ObjectOptimisticLockingFailureException
        || e instanceof BookAlreadyOnHoldException
        || (e.getCause() instanceof jakarta.persistence.OptimisticLockException);
  }

  /**
   * Repositorio de prueba (solo en tests). No modifica código productivo.
   * Implementa el puerto y expone setupPatron(...) para inicializar datos en memoria.
   */
  private static class TestPatronLendingRepository implements PatronLendingRepository {

    private final Map<UUID, Patron> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Patron> findById(UUID patronId) {
      return Optional.ofNullable(storage.get(patronId));
    }

    /** Helper de test para insertar un Patron en el repositorio */
    public void setupPatron(Patron patron) {
      storage.put(patron.getId(), patron);
    }

    /** Limpia entre tests si hiciera falta */
    @SuppressWarnings("unused")
    public void clear() {
      storage.clear();
    }
  }
}