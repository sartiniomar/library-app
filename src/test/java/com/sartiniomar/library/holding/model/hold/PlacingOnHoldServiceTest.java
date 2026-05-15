package com.sartiniomar.library.holding.model.hold;

import com.sartiniomar.library.holding.model.book.BookInstance;
import com.sartiniomar.library.holding.model.patron.Patron;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class PlacingOnHoldServiceTest {

  @Test
  void should_place_hold_when_all_conditions_are_met() {

    Patron patron = Patron.regular();
    BookInstance book = BookInstance.circulating("book-1");

    PlacingOnHoldService service = new PlacingOnHoldService();

    DomainResult<Hold> result = service.placeOnHold(patron, book);

    // ✅ resultado no nulo
    assertNotNull(result);
    assertNotNull(result.result());

    Hold hold = result.result();

    // ✅ validar el Hold creado
    assertEquals(patron.getId(), hold.getPatronId());
    assertEquals(book.getId(), hold.getBookInstanceId());
    assertNotNull(hold.getId());

    // ✅ validar evento
    assertEquals(1, result.events().size());
    assertInstanceOf(BookPlacedOnHoldEvent.class, result.events().get(0));
  }

}
