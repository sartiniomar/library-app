package com.sartiniomar.library.loan.support.builder;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import java.time.Instant;
import java.util.UUID;

public class LoanTestDataBuilder {

  public Loan buildLoan(
      UUID id,
      UUID patronId,
      UUID bookInstanceId,
      LoanStatus status,
      Instant reservedAt,
      Instant lentAt,
      Instant dueAt,
      Instant returnedAt) {
    return Loan.withId(
        id,
        patronId,
        bookInstanceId,
        status,
        reservedAt,
        lentAt,
        dueAt,
        returnedAt
    );
  }

  public Loan buildDefaultReserve() {
    return Loan.createReserve(
        UUID.fromString("00000000-1111-2222-3333-444444444444"),
        UUID.fromString("55555555-6666-7777-8888-999999999999"),
        java.time.Clock.systemDefaultZone());
  }

  public Loan buildDefaultCheckout() {
    return Loan.createLent(
        UUID.fromString("00000000-1111-2222-3333-444444444444"),
        UUID.fromString("55555555-6666-7777-8888-999999999999"),
        java.time.Clock.systemDefaultZone(),
        7);
  }

  public Loan buildReserve(Patron patron, BookInstance bookInstance) {
    return Loan.createReserve(
        patron.getId(),
        bookInstance.getId(),
        java.time.Clock.systemDefaultZone());
  }

  public Loan buildCheckout(Patron patron, BookInstance bookInstance) {
    return Loan.createLent(
        patron.getId(),
        bookInstance.getId(),
        java.time.Clock.systemDefaultZone(),
        patron.getLimitDays());
  }

  public Patron buildDefaultPatron(PatronType patronType) {
    return new Patron(
        UUID.fromString("00000000-1111-2222-3333-444444444444"),
        patronType
    );
  }

  public BookInstance buildDefaultBookInstance(BookType bookType, BookInstanceStatus bookInstanceStatus) {
    return new BookInstance(
        UUID.fromString("55555555-6666-7777-8888-999999999999"),
        UUID.randomUUID(),
        bookType,
        bookInstanceStatus
    );
  }

  public BookInstance buildBookInstance(UUID bookInstanceId, UUID bookId, BookType bookType, BookInstanceStatus bookInstanceStatus) {
    return new BookInstance(
        bookInstanceId,
        bookId,
        bookType,
        bookInstanceStatus
    );
  }
}
