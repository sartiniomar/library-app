package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotAvailableException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanBookEvent;
import com.sartiniomar.library.loan.domain.loan.OnlyResearcherCanLoanRestrictedBooksException;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckoutServiceTest {

  private static Stream<Arguments> provideDataForBookInstancesStateAreNotAvailable() {
    return Stream.of(
        Arguments.of("LENT"),
        Arguments.of("UNAVAILABLE")
    );
  }

  @Test
  void should_checkout_when_all_conditions_are_met_and_available_book() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    CheckoutService service = new CheckoutService();
    DomainResult<Loan> result = service.checkout(patron, book);

    assertNotNull(result);
    assertNotNull(result.result());

    Loan loan = result.result();

    assertNotNull(loan.getId());
    assertEquals(patron.getId(), loan.getPatronId());
    assertEquals(book.getId(), loan.getBookInstanceId());
    assertEquals(BookInstanceStatus.LENT, book.getStatus());
    assertTrue(Duration.between(loan.getLentAt(), Instant.now()).abs().toMillis() < 1000);

    assertEquals(1, result.events().size());
    assertInstanceOf(LoanBookEvent.class, result.events().getFirst());
  }

  @Test
  void should_checkout_when_all_conditions_are_met_and_reserved_book() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.RESERVED);

    CheckoutService service = new CheckoutService();
    DomainResult<Loan> result = service.checkout(patron, book);

    assertNotNull(result);
    assertNotNull(result.result());

    Loan loan = result.result();

    assertNotNull(loan.getId());
    assertEquals(patron.getId(), loan.getPatronId());
    assertEquals(book.getId(), loan.getBookInstanceId());
    assertEquals(BookInstanceStatus.LENT, book.getStatus());
    assertTrue(Duration.between(loan.getLentAt(), Instant.now()).abs().toMillis() < 1000);

    assertEquals(1, result.events().size());
    assertInstanceOf(LoanBookEvent.class, result.events().getFirst());
  }

  @Test void should_throw_exception_when_book_is_restricted_and_patron_is_regular() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.RESTRICTED, BookInstanceStatus.AVAILABLE);
    CheckoutService service = new CheckoutService();

    OnlyResearcherCanLoanRestrictedBooksException ex =
        assertThrows(OnlyResearcherCanLoanRestrictedBooksException.class,
            () -> service.checkout(patron, book)
        );

    assertEquals("Only Researcher Can Loan Restricted Books!", ex.getMessage());
  }

  @ParameterizedTest
  @MethodSource("provideDataForBookInstancesStateAreNotAvailable")
  @SneakyThrows
  void should_throw_exception_when_book_is_not_available(BookInstanceStatus state) {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, state);
    CheckoutService service = new CheckoutService();

    BookInstanceNotAvailableException ex =
        assertThrows(BookInstanceNotAvailableException.class,
            () -> service.checkout(patron, book)
        );

    assertEquals("Book Already Unavailable!", ex.getMessage());
  }
}
