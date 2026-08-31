package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotAvailableException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CancelledServiceDomainTest {

  private static Stream<Arguments> provideDataForLoanStateAreNotAvailableForCancelled() {
    return Stream.of(
        Arguments.of("CANCELLED"),
        Arguments.of("LENT"),
        Arguments.of("RETURNED"),
        Arguments.of("DELAYED"),
        Arguments.of("RETURNED_WITH_DELAY")
    );
  }

  @Test
  void should_cancelled_when_all_conditions_are_met() {
    UUID bookId = UUID.randomUUID();
    UUID bookInstanceId = UUID.randomUUID();
    UUID patronId = UUID.randomUUID();
    Patron patron = new Patron(patronId, PatronType.REGULAR);
    BookInstance bookInstance = new BookInstance(
        bookInstanceId, bookId, BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = new Loan(patronId, bookInstanceId, LoanStatus.RESERVED, Instant.now(), null, null, null);

    CancelServiceDomain service = new CancelServiceDomain();
    Loan result = service.cancel(loan, bookInstance);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(patron.getId(), result.getPatronId());
    assertEquals(bookInstance.getId(), result.getBookInstanceId());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertEquals(LoanStatus.CANCELLED, result.getStatus());
  }

  @ParameterizedTest
  @MethodSource("provideDataForLoanStateAreNotAvailableForCancelled")
  @SneakyThrows
  void should_throw_exception_when_loan_is_not_available_cancelled(LoanStatus status) {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), status, Instant.now(), null, null, null);
    CancelServiceDomain service = new CancelServiceDomain();

    BookInstanceNotAvailableException ex =
        assertThrows(BookInstanceNotAvailableException.class,
            () -> service.cancel(loan, bookInstance)
        );
    assertEquals("The loan is not reserved!", ex.getMessage());
  }
}
