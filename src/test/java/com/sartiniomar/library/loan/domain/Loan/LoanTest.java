package com.sartiniomar.library.loan.domain.Loan;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanTest {

  @Test
  void should_create_successfuly_hold() {
    Patron patron = Patron.regular();
    BookInstance book = BookInstance.circulating(UUID.randomUUID());

    Loan hold = new Loan(patron.getId(), book.getId());

    assertNotNull(hold.getId());
    assertEquals(patron.getId(), hold.getPatronId());
    assertEquals(book.getId(), hold.getBookInstanceId());
  }

}
