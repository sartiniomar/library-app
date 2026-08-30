package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotAvailableException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.OnlyResearcherCanLoanRestrictedBooksException;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReserveServiceTest {

  private static final Integer RESERVED_LIMIT_DAYS = 3;

  private static Stream<Arguments> provideDataForBookInstancesStateAreNotAvailable() {
    return Stream.of(
        Arguments.of("RESERVED"),
        Arguments.of("LENT"),
        Arguments.of("UNAVAILABLE")
    );
  }

  @Test
  void should_reserve_when_all_conditions_are_met() {
    Instant now = Instant.parse("2026-08-27T19:00:00Z");
    Clock clock = Clock.fixed(now, ZoneOffset.UTC);

    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);

    BookInstance book = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    ReserveServiceDomain service = new ReserveServiceDomain(clock);
    Loan result = service.reserve(patron, book);

    assertNotNull(result);

    assertNotNull(result.getId());
    assertEquals(patron.getId(), result.getPatronId());
    assertEquals(book.getId(), result.getBookInstanceId());
    assertEquals(BookInstanceStatus.RESERVED, book.getStatus());
    assertEquals(now, result.getReservedAt());
    assertEquals(now.plus(Duration.ofDays(RESERVED_LIMIT_DAYS)), result.getDueAt());
  }

  @Test void should_throw_exception_when_book_is_restricted_and_patron_is_regular() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.RESTRICTED, BookInstanceStatus.AVAILABLE);
    ReserveServiceDomain service = new ReserveServiceDomain(Clock.systemDefaultZone());

    OnlyResearcherCanLoanRestrictedBooksException ex =
        assertThrows(OnlyResearcherCanLoanRestrictedBooksException.class,
            () -> service.reserve(patron, book)
        );

    assertEquals("Only Researcher Can Loan Restricted Books!", ex.getMessage());
  }

  @ParameterizedTest
  @MethodSource("provideDataForBookInstancesStateAreNotAvailable")
  @SneakyThrows
  void should_throw_exception_when_book_is_not_available(BookInstanceStatus state) {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, state);
    ReserveServiceDomain service = new ReserveServiceDomain(Clock.systemDefaultZone());

    BookInstanceNotAvailableException ex =
        assertThrows(BookInstanceNotAvailableException.class,
            () -> service.reserve(patron, book)
        );

    assertEquals("Book Already Unavailable!", ex.getMessage());
  }
}
