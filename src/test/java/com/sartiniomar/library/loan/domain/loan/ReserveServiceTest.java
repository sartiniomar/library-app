package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ReserveServiceTest {

  @Test
  void should_place_hold_when_all_conditions_are_met() {

    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, false);

    ReserveService service = new ReserveService();

    DomainResult<Loan> result = service.reserve(patron, book);

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
    assertInstanceOf(ReserveBookEvent.class, result.events().get(0));
  }

}
