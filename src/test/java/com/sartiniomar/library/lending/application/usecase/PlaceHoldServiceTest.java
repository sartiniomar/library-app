package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.support.InMemoryEventPublisher;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryBookInstanceRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryHoldRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryPatronLendingRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.hold.BookPlacedOnHoldEvent;
import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceHoldServiceTest {

  // Helper solo para tests — acceso by reflection (sin tocar código productivo)
  private void setupPatronInRepo(InMemoryPatronLendingRepository repo, Patron patron) {
    try {
      java.lang.reflect.Field storageField = InMemoryPatronLendingRepository.class.getDeclaredField("storage");
      storageField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<UUID, Patron> storage = (Map<UUID, Patron>) storageField.get(repo);
      storage.put(patron.getId(), patron);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void should_place_hold_via_use_case() {
    InMemoryPatronLendingRepository patronRepo = new InMemoryPatronLendingRepository();
    InMemoryBookInstanceRepository bookRepo = new InMemoryBookInstanceRepository();
    InMemoryHoldRepository holdRepo = new InMemoryHoldRepository();

    Patron patron = Patron.regular();
    BookInstance book = BookInstance.circulating("book-1");

    setupPatronInRepo(patronRepo, patron);  // ← Setup solo aquí, sin tocar productivo
    bookRepo.save(book);

    InMemoryEventPublisher publisher = new InMemoryEventPublisher();
    PlacingOnHoldService service = new PlacingOnHoldService();

    PlaceHoldService useCase =
        new PlaceHoldService(patronRepo, bookRepo, holdRepo, publisher, service);

    useCase.execute(new PlaceHoldCommand(patron.getId(), book.getId()));

    assertEquals(1, holdRepo.countByPatronId(patron.getId()));
    assertEquals(1, publisher.events.size());
  }

  @Test
  void should_publish_event_when_hold_is_created() {
    InMemoryPatronLendingRepository patronRepo = new InMemoryPatronLendingRepository();
    InMemoryBookInstanceRepository bookRepo = new InMemoryBookInstanceRepository();
    InMemoryHoldRepository holdRepo = new InMemoryHoldRepository();

    Patron patron = Patron.regular();
    setupPatronInRepo(patronRepo, patron);  // ← Setup solo aquí

    BookInstance book = BookInstance.circulating("book-1");
    bookRepo.save(book);

    InMemoryEventPublisher publisher = new InMemoryEventPublisher();
    PlacingOnHoldService service = new PlacingOnHoldService();

    PlaceHoldService useCase = new PlaceHoldService(patronRepo, bookRepo, holdRepo, publisher, service);

    useCase.execute(new PlaceHoldCommand(patron.getId(), book.getId()));

    assertEquals(1, publisher.events.size());
    assertInstanceOf(BookPlacedOnHoldEvent.class, publisher.events.getFirst());
  }
}