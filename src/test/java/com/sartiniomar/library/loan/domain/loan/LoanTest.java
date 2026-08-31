package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoanTest {

  private static final Integer RESERVED_LIMIT_DAYS = 3;
  private static final Integer REGULAR_PATRON_LEND_LIMIT_DAYS = 7;
  private static final Integer RESEARCHER_PATRON_LEND_LIMIT_DAYS = 14;

  private static Stream<Arguments> provideDataForGroupCancelledAndLentChangeStatusError() {
    return Stream.of(
        Arguments.of("CANCELLED"),
        Arguments.of("LENT"),
        Arguments.of("RETURNED"),
        Arguments.of("DELAYED"),
        Arguments.of("RETURNED_WITH_DELAY")
    );
  }

  private static Stream<Arguments> provideDataForGroupReturnedChangeStatusError() {
    return Stream.of(
        Arguments.of("CANCELLED"),
        Arguments.of("RESERVED"),
        Arguments.of("RETURNED"),
        Arguments.of("RETURNED_WITH_DELAY")
    );
  }

  private static Stream<Arguments> provideDataForGroupDelayedChangeStatusError() {
    return Stream.of(
        Arguments.of("CANCELLED"),
        Arguments.of("RESERVED"),
        Arguments.of("RETURNED"),
        Arguments.of("DELAYED"),
        Arguments.of("RETURNED_WITH_DELAY")
    );
  }

  @Test
  void should_create_successfully_reserve() {
    Instant now = Instant.parse("2026-08-27T19:00:00Z");
    Clock clock = Clock.fixed(now, ZoneOffset.UTC);

    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);

    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.RESERVED);

    Loan loan = Loan.createReserve(patron.getId(), book.getId(), clock);

    assertNotNull(loan.getId());
    assertEquals(patron.getId(), loan.getPatronId());
    assertEquals(book.getId(), loan.getBookInstanceId());
    assertEquals(LoanStatus.RESERVED, loan.getStatus());
    assertEquals(now, loan.getReservedAt());
    assertEquals(now.plus(Duration.ofDays(RESERVED_LIMIT_DAYS)), loan.getDueAt());
    assertNotNull(loan.getReservedAt());
  }

  @Test
  void should_create_successfully_lent_for_Regular_Patron() {
    Instant now = Instant.parse("2026-08-27T19:00:00Z");
    Clock clock = Clock.fixed(now, ZoneOffset.UTC);

    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);

    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    Loan loan = Loan.createLent(patron.getId(), book.getId(), clock, REGULAR_PATRON_LEND_LIMIT_DAYS);

    assertNotNull(loan.getId());
    assertEquals(patron.getId(), loan.getPatronId());
    assertEquals(book.getId(), loan.getBookInstanceId());
    assertEquals(LoanStatus.LENT, loan.getStatus());
    assertEquals(now, loan.getLentAt());
    assertEquals(now.plus(Duration.ofDays(REGULAR_PATRON_LEND_LIMIT_DAYS)), loan.getDueAt());
    assertNotNull(loan.getLentAt());
  }

  @Test
  void should_create_successfully_lent_for_Researcher_Patron() {
    Instant now = Instant.parse("2026-08-27T19:00:00Z");
    Clock clock = Clock.fixed(now, ZoneOffset.UTC);

    Patron patron = new Patron(UUID.randomUUID(), PatronType.RESEARCHER);

    BookInstance book = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    Loan loan = Loan.createLent(patron.getId(), book.getId(), clock, RESEARCHER_PATRON_LEND_LIMIT_DAYS);

    assertNotNull(loan.getId());
    assertEquals(patron.getId(), loan.getPatronId());
    assertEquals(book.getId(), loan.getBookInstanceId());
    assertEquals(LoanStatus.LENT, loan.getStatus());
    assertEquals(now, loan.getLentAt());
    assertEquals(now.plus(Duration.ofDays(RESEARCHER_PATRON_LEND_LIMIT_DAYS)), loan.getDueAt());
    assertNotNull(loan.getLentAt());
  }

  @Test
  void should_change_reserved_to_cancelled_status() {
    Loan loan = Loan.createReserve(UUID.randomUUID(), UUID.randomUUID(), Clock.systemDefaultZone());
    loan.cancelled();
    assertEquals(LoanStatus.CANCELLED, loan.getStatus());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupCancelledAndLentChangeStatusError")
  @SneakyThrows
  void should_throw_exception_when_loan_status_is_not_allow_for_cancelled(LoanStatus status) {
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), status, Instant.now(), null, null, null);

    assertThrows(TransitionStatusException.class, loan::cancelled);
  }

  @Test
  void should_change_reserved_to_lent_status() {
    Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    Loan loan = Loan.createReserve(UUID.randomUUID(), UUID.randomUUID(), clock);
    loan.lent(Patron.REGULAR_PATRON_LEND_LIMIT_DAYS, clock);
    assertEquals(LoanStatus.LENT, loan.getStatus());
    assertTrue(Duration.between(loan.getLentAt(), Instant.now(clock)).abs().toMillis() < 1000);
    assertTrue(Duration.between(loan.getDueAt(),
        Instant.now(clock).plus(Duration.ofDays(patron.getLimitDays()))).abs().toMillis() < 1000);
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupCancelledAndLentChangeStatusError")
  @SneakyThrows
  void should_throw_exception_when_loan_status_is_not_allow_for_lent(LoanStatus status) {
    Loan loan = new Loan(
        UUID.randomUUID(),
        UUID.randomUUID(),
        status,
        Instant.now(),
        null,
        null,
        null
    );

    assertThrows(
        TransitionStatusException.class,
        () -> loan.lent(Patron.REGULAR_PATRON_LEND_LIMIT_DAYS, Clock.systemDefaultZone())
    );
  }

  @Test
  void should_change_lent_to_returned_status() {
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), LoanStatus.LENT, Instant.now(), null, null, null);
    loan.returned();
    assertEquals(LoanStatus.RETURNED, loan.getStatus());
    assertTrue(Duration.between(loan.getReturnedAt(), Instant.now()).abs().toMillis() < 1000);
  }

  @Test
  void should_change_delayed_to_returned_with_delay_status() {
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), LoanStatus.DELAYED, Instant.now(), null, null, null);
    loan.returned();
    assertEquals(LoanStatus.RETURNED_WITH_DELAY, loan.getStatus());
    assertTrue(Duration.between(loan.getReturnedAt(), Instant.now()).abs().toMillis() < 1000);
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupReturnedChangeStatusError")
  @SneakyThrows
  void should_throw_exception_when_loan_status_is_not_allow_for_returned(LoanStatus status) {
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), status, Instant.now(), null, null, null);

    assertThrows(TransitionStatusException.class, loan::returned);
  }

  @Test
  void should_change_lent_to_delayed_status() {
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), LoanStatus.LENT, Instant.now(), null, null, null);
    loan.delayed();
    assertEquals(LoanStatus.DELAYED, loan.getStatus());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupDelayedChangeStatusError")
  @SneakyThrows
  void should_throw_exception_when_loan_status_is_not_allow_for_delayed(LoanStatus status) {
    Loan loan = new Loan(UUID.randomUUID(), UUID.randomUUID(), status, Instant.now(), null, null, null);

    assertThrows(TransitionStatusException.class, loan::delayed);
  }
}
