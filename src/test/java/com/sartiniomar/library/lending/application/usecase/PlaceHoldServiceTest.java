package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.support.InMemoryEventPublisher;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryBookInstanceRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryHoldRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.inMemory.InMemoryPatronRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.hold.BookPlacedOnHoldEvent;
import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaceHoldServiceTest {

  @Test
  void should_place_hold_via_use_case() {

    InMemoryPatronRepository patronRepo = new InMemoryPatronRepository();
    InMemoryBookInstanceRepository bookRepo = new InMemoryBookInstanceRepository();
    InMemoryHoldRepository holdRepo = new InMemoryHoldRepository();

    Patron patron = Patron.regular();
    BookInstance book = BookInstance.circulating("book-1");

    patronRepo.save(patron);
    bookRepo.save(book);

    InMemoryEventPublisher publisher = new InMemoryEventPublisher();
    PlacingOnHoldService service = new PlacingOnHoldService();

    PlaceHoldService useCase =
        new PlaceHoldService(patronRepo, bookRepo, holdRepo, publisher, service);

    useCase.execute(new PlaceHoldCommand(patron.getId(), book.getId()));

    // ✅ ASSERT PRINCIPAL: se creó el hold
    assertEquals(1, holdRepo.countByPatronId(patron.getId()));

    // (opcional) validar contenido
    /*Hold hold = holdRepo.findAll().get(0);
    assertEquals("patron-1", hold.getPatronId());
    assertEquals("book-1", hold.getBookId());*/

    // ✅ ASSERT EVENTO
    assertEquals(1, publisher.events.size());
  }

  @Test
  void should_publish_event_when_hold_is_created() {
    InMemoryPatronRepository patronRepo = new InMemoryPatronRepository();
    InMemoryBookInstanceRepository bookRepo = new InMemoryBookInstanceRepository();
    InMemoryHoldRepository holdRepo = new InMemoryHoldRepository();

    Patron patron = Patron.regular();
    patronRepo.save(patron);
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
