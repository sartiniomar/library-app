package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CheckoutReserveServiceDomainTest {

  @Test
  void should_checkout_reserve_when_all_conditions_are_met_and_available_book() {
    Instant now = Instant.parse("2026-08-27T19:00:00Z");
    Clock clock = Clock.fixed(now, ZoneOffset.UTC);

    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);

    BookInstance book = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    Loan loan = new Loan(
        patron.getId(),
        book.getId(),
        LoanStatus.RESERVED,
        now,
        null,
        now.plus(Duration.ofDays(patron.getLimitDays())),
        null
    );

    CheckoutReserveServiceDomain service = new CheckoutReserveServiceDomain(clock);
    Loan result = service.checkoutReserve(loan, patron, book);

    assertNotNull(result);
    assertEquals(loan.getId(), result.getId());
    assertEquals(patron.getId(), result.getPatronId());
    assertEquals(book.getId(), result.getBookInstanceId());
    assertEquals(now, result.getLentAt());
    assertEquals(now.plus(Duration.ofDays(Patron.REGULAR_PATRON_LEND_LIMIT_DAYS)), result.getDueAt());
    assertEquals(BookInstanceStatus.LENT, book.getStatus());
  }

}
