package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanTest {

  @Test
  void should_create_successfully_reserve() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.RESERVED);

    Loan loan = Loan.createReserve(patron.getId(), book.getId());

    assertNotNull(loan.getId());
    assertEquals(patron.getId(), loan.getPatronId());
    assertEquals(book.getId(), loan.getBookInstanceId());
    assertEquals(LoanStatus.RESERVED, loan.getStatus());
    assertNotNull(loan.getReservedAt());
  }

  @Test
  void should_create_successfully_lent() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    Loan loan = Loan.createLent(patron.getId(), book.getId());

    assertNotNull(loan.getId());
    assertEquals(patron.getId(), loan.getPatronId());
    assertEquals(book.getId(), loan.getBookInstanceId());
    assertEquals(LoanStatus.LENT, loan.getStatus());
    assertNotNull(loan.getLentAt());
  }

}
