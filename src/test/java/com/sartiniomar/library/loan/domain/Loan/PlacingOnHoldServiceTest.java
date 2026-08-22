package com.sartiniomar.library.loan.domain.Loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.BookPlacedOnHoldEvent;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.PlacingOnHoldService;
import com.sartiniomar.library.loan.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class PlacingOnHoldServiceTest {

  @Test
  void should_place_hold_when_all_conditions_are_met() {

    Patron patron = Patron.regular();
    BookInstance book = BookInstance.circulating(UUID.randomUUID());

    PlacingOnHoldService service = new PlacingOnHoldService();

    DomainResult<Loan> result = service.placeOnHold(patron, book);

    // ✅ resultado no nulo
    assertNotNull(result);
    assertNotNull(result.result());

    Loan hold = result.result();

    // ✅ validar el Loan creado
    assertEquals(patron.getId(), hold.getPatronId());
    assertEquals(book.getId(), hold.getBookInstanceId());
    assertNotNull(hold.getId());

    // ✅ validar evento
    assertEquals(1, result.events().size());
    assertInstanceOf(BookPlacedOnHoldEvent.class, result.events().get(0));
  }

}
