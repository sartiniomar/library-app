package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.application.port.out.PatronRepository;
import com.sartiniomar.library.lending.domain.hold.BookAlreadyOnHoldException;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest
class PlaceHoldConcurrencyTest {

  @Autowired
  private PlaceHoldUseCase placeHoldUseCase;

  @Autowired
  private BookInstanceRepository bookInstanceRepository;

  @Autowired
  private PatronRepository patronRepository;

  //@Test
  void shouldHandleConcurrency() throws Exception {

    BookInstance book = BookInstance.circulating("book-1");
    bookInstanceRepository.save(book);
    Patron patron = Patron.researcher();
    patronRepository.save(patron);

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch ready = new CountDownLatch(2);
    CountDownLatch start = new CountDownLatch(1);

    AtomicInteger success = new AtomicInteger();
    AtomicInteger failures = new AtomicInteger();

    Runnable task = () -> {
      ready.countDown();
      try {
        start.await();

        placeHoldUseCase.execute(
            new PlaceHoldCommand(patron.getId(), book.getId())
        );

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
    executor.awaitTermination(5, TimeUnit.SECONDS);

    assertEquals(2, success.get() + failures.get());
    assertTrue(success.get() >= 1);
    assertTrue(failures.get() >= 1);
  }

  private boolean isConcurrencyRelated(Throwable e) {
    return e instanceof org.springframework.orm.ObjectOptimisticLockingFailureException
        || e instanceof BookAlreadyOnHoldException
        || (e.getCause() instanceof jakarta.persistence.OptimisticLockException);
  }
}