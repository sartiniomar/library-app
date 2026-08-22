package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.infrastructure.persistence.inMemory.adapter.PatronInMemoryRepository;
import com.sartiniomar.library.loan.domain.patron.Patron;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

public class PlaceHoldServiceTest {

  private void setupPatronInRepo(PatronInMemoryRepository repo, Patron patron) {
    try {
      Field storageField = PatronInMemoryRepository.class.getDeclaredField("storage");
      storageField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<UUID, Patron> storage = (Map<UUID, Patron>) storageField.get(repo);
      storage.put(patron.getId(), patron);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /*@Test
  void should_place_hold_via_use_case() {
    PatronLendingInMemoryRepository patronRepo = new PatronLendingInMemoryRepository();
    BookInstanceInMemoryRepository bookRepo = new BookInstanceInMemoryRepository();
    HoldInMemoryRepository holdRepo = new HoldInMemoryRepository();

    Patron patron = Patron.regular();
    BookInstance book = BookInstance.circulating(UUID.randomUUID());

    setupPatronInRepo(patronRepo, patron);
    bookRepo.save(book);

    InMemoryEventPublisher publisher = new InMemoryEventPublisher();
    PlacingOnHoldService service = new PlacingOnHoldService();

    PlaceHoldService useCase =
        new PlaceHoldService(patronRepo, bookRepo, holdRepo, publisher, service);

    useCase.execute(new PlaceHoldCommand(patron.getId(), book.getId()));

    assertEquals(1, holdRepo.countByPatronId(patron.getId()));
    assertEquals(1, publisher.events.size());
  }*/

  /*@Test
  void should_publish_event_when_hold_is_created() {
    PatronLendingInMemoryRepository patronRepo = new PatronLendingInMemoryRepository();
    BookInstanceInMemoryRepository bookRepo = new BookInstanceInMemoryRepository();
    HoldInMemoryRepository holdRepo = new HoldInMemoryRepository();

    Patron patron = Patron.regular();
    setupPatronInRepo(patronRepo, patron);  // ← Setup solo aquí

    BookInstance book = BookInstance.circulating(UUID.randomUUID());
    bookRepo.save(book);

    InMemoryEventPublisher publisher = new InMemoryEventPublisher();
    PlacingOnHoldService service = new PlacingOnHoldService();

    PlaceHoldService useCase = new PlaceHoldService(patronRepo, bookRepo, holdRepo, publisher, service);

    useCase.execute(new PlaceHoldCommand(patron.getId(), book.getId()));

    assertEquals(1, publisher.events.size());
    assertInstanceOf(BookPlacedOnHoldEvent.class, publisher.events.getFirst());
  }*/
}