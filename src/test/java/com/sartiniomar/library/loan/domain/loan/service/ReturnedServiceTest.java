package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotAvailableException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanBookEvent;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReturnedServiceTest {

  private static Stream<Arguments> provideDataForLoanStateAreNotAvailableForReturned() {
    return Stream.of(
        Arguments.of("RESERVED"),
        Arguments.of("CANCELLED"),
        Arguments.of("RETURNED"),
        Arguments.of("RETURNED_WITH_DELAY")
    );
  }

  @Test
  void should_returned_when_all_conditions_are_met_for_lent() {
    UUID bookId = UUID.randomUUID();
    UUID bookInstanceId = UUID.randomUUID();
    UUID patronId = UUID.randomUUID();
    Patron patron = new Patron(patronId, PatronType.REGULAR);
    BookInstance bookInstance = new BookInstance(
        bookInstanceId, bookId, BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = new Loan(patronId, bookInstanceId, LoanStatus.LENT, Instant.now(), null, null, null);

    ReturnedServiceDomain service = new ReturnedServiceDomain();
    DomainResult<Loan> result = service.returned(loan, patron, bookInstance);

    assertNotNull(result);
    assertNotNull(result.result());

    Loan loanResult = result.result();

    assertNotNull(loanResult.getId());
    assertEquals(patron.getId(), loanResult.getPatronId());
    assertEquals(bookInstance.getId(), loanResult.getBookInstanceId());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertEquals(LoanStatus.RETURNED, loanResult.getStatus());
    assertTrue(Duration.between(loan.getReturnedAt(), Instant.now()).abs().toMillis() < 1000);

    assertEquals(1, result.events().size());
    assertInstanceOf(LoanBookEvent.class, result.events().getFirst());
  }

  @Test
  void should_returned_when_all_conditions_are_met_for_delayed() {
    UUID bookId = UUID.randomUUID();
    UUID bookInstanceId = UUID.randomUUID();
    UUID patronId = UUID.randomUUID();
    Patron patron = new Patron(patronId, PatronType.REGULAR);
    BookInstance bookInstance = new BookInstance(
        bookInstanceId, bookId, BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = new Loan(patronId, bookInstanceId, LoanStatus.DELAYED, Instant.now(), null, null, null);

    ReturnedServiceDomain service = new ReturnedServiceDomain();
    DomainResult<Loan> result = service.returned(loan, patron, bookInstance);

    assertNotNull(result);
    assertNotNull(result.result());

    Loan loanResult = result.result();

    assertNotNull(loanResult.getId());
    assertEquals(patron.getId(), loanResult.getPatronId());
    assertEquals(bookInstance.getId(), loanResult.getBookInstanceId());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertEquals(LoanStatus.RETURNED_WITH_DELAY, loanResult.getStatus());
    assertTrue(Duration.between(loan.getReturnedAt(), Instant.now()).abs().toMillis() < 1000);

    assertEquals(1, result.events().size());
    assertInstanceOf(LoanBookEvent.class, result.events().getFirst());
  }

  @ParameterizedTest
  @MethodSource("provideDataForLoanStateAreNotAvailableForReturned")
  @SneakyThrows
  void should_throw_exception_when_loan_is_not_available_returned(LoanStatus status) {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), status, Instant.now(), null, null, null);
    ReturnedServiceDomain service = new ReturnedServiceDomain();

    BookInstanceNotAvailableException ex =
        assertThrows(BookInstanceNotAvailableException.class,
            () -> service.returned(loan, patron, bookInstance)
        );
    assertEquals("The loan is not lent or delayed!", ex.getMessage());
  }
}
