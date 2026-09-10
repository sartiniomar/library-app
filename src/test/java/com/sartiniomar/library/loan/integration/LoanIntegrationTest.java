package com.sartiniomar.library.loan.integration;

import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.integration.support.factory.BookInstanceLoanTestFactory;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import com.sartiniomar.library.loan.infrastructure.web.dto.LoanResponse;
import com.sartiniomar.library.loan.integration.support.LoanHttpHelper;
import com.sartiniomar.library.loan.integration.support.factory.LoanTestFactory;
import com.sartiniomar.library.loan.integration.support.factory.PatronLoanTestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.jdbc.JdbcTestUtils;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanIntegrationTest extends LoanHttpHelper {

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private LoanTestFactory loanTestFactory;

  @Autowired
  private PatronLoanTestFactory patronTestFactory;

  @Autowired
  private BookInstanceLoanTestFactory bookInstanceTestFactory;

  @Autowired
  private BookInstanceLoanRepository bookInstanceLoanRepository;

  private static Stream<Arguments> provideUnavailableReservedCheckoutStatus() {
    return Stream.of(
        Arguments.of(BookInstanceStatus.UNAVAILABLE),
        Arguments.of(BookInstanceStatus.RESERVED),
        Arguments.of(BookInstanceStatus.LENT)
    );
  }

  private static Stream<Arguments> provideUnavailableLoanStatusForCancelOrCheckout() {
    return Stream.of(
        Arguments.of(LoanStatus.CANCELLED),
        Arguments.of(LoanStatus.LENT),
        Arguments.of(LoanStatus.DELAYED),
        Arguments.of(LoanStatus.RETURNED),
        Arguments.of(LoanStatus.RETURNED_WITH_DELAY)
    );
  }

  private static Stream<Arguments> provideUnavailableLoanStatusForReturn() {
    return Stream.of(
        Arguments.of(LoanStatus.CANCELLED),
        Arguments.of(LoanStatus.RESERVED),
        Arguments.of(LoanStatus.RETURNED),
        Arguments.of(LoanStatus.RETURNED_WITH_DELAY)
    );
  }

  @Test
  void shouldCreateLoanReserve() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");

    LoanResponse response = returnIsCreateWhenCreateReserve();
    flushAndClear();

    Loan loan = loanRepository.findById(response.id()).orElseThrow();
    BookInstance bookInstanceUpdate =
        bookInstanceLoanRepository.findById(response.bookInstanceId()).orElseThrow();

    assertThat(loan)
        .extracting("patronId","bookInstanceId", "status", "reservedAt")
        .containsExactly(patron.getId(), bookInstance.getId(), loan.getStatus(), loan.getReservedAt());

    assertEquals(patron.getId(), response.patronId());
    assertEquals(bookInstance.getId(), response.bookInstanceId());
    assertEquals(loan.getStatus(), response.status());
    assertEquals(
        loan.getReservedAt().truncatedTo(ChronoUnit.MILLIS),
        response.reservedAt().truncatedTo(ChronoUnit.MILLIS)
    );
    assertEquals(LoanStatus.RESERVED, loan.getStatus());
    assertEquals(BookInstanceStatus.RESERVED, bookInstanceUpdate.getStatus());

    assertEquals(initialCount + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
  }

  @Test
  void shouldReturnNotFoundWhenPatronIdNotFoundInLoanReserveCreation() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenCreateReserve();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Patron not found: " + DEFAULT_PATRON_ID, response.errors().getFirst().description());
  }

  @Test
  void shouldReturnNotFoundWhenBookInstanceIdNotFoundInLoanReserveCreation() throws Exception {
    patronTestFactory.createDefaultPatron(PatronType.REGULAR);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenCreateReserve();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found: " + DEFAULT_BOOK_INSTANCE_ID, response.errors().getFirst().description());
  }

  @Test
  void shouldReturnConflictWhenLoanLimitExceededInLoanReserveCreation() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);

    BookInstance bookInstance1 = bookInstanceTestFactory.createBookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    BookInstance bookInstance2 = bookInstanceTestFactory.createBookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    BookInstance bookInstance3 = bookInstanceTestFactory.createBookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    BookInstance bookInstance4 = bookInstanceTestFactory.createDefaultBookInstance(
        BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    loanTestFactory.createLoanReserve(patron, bookInstance1);
    loanTestFactory.createLoanReserve(patron, bookInstance2);
    loanTestFactory.createLoanReserve(patron, bookInstance3);

    entityManager.flush();
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    entityManager.clear();

    ErrorResponse response = returnConflictWhenCreateReserve();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("409 CONFLICT", response.code());
    assertEquals("Loan Limit Exceeded.", response.errors().getFirst().description());

    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance4.getStatus());
  }

  @Test
  void shouldReturnConflictWhenOnlyResearcherCanLoanRestrictedBooks() throws Exception {
    patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance = bookInstanceTestFactory.createDefaultBookInstance(BookType.RESTRICTED, BookInstanceStatus.AVAILABLE);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnConflictWhenCreateReserve();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("409 CONFLICT", response.code());
    assertEquals("Only Researcher Can Loan Restricted Books!", response.errors().getFirst().description());

    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @MethodSource("provideUnavailableReservedCheckoutStatus")
  @ParameterizedTest
  void shouldReturnConflictWhenBookInstanceNotAvailableExceptionIsThrown(BookInstanceStatus bookInstanceStatus) throws Exception {
    patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance = bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, bookInstanceStatus);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnConflictWhenCreateReserve();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("409 CONFLICT", response.code());
    assertEquals("Book Already Unavailable!", response.errors().getFirst().description());

    assertEquals(bookInstanceStatus, bookInstance.getStatus());
  }

  @Test
  void shouldCancelLoanReserve() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = loanTestFactory.createLoanReserve(patron, bookInstance);
    entityManager.flush();

    LoanResponse response = returnOkWhenCancelReserve(loan.getId());
    flushAndClear();

    Loan loanUpdate = loanRepository.findById(response.id()).orElseThrow();
    BookInstance bookInstanceUpdate =
        bookInstanceLoanRepository.findById(response.bookInstanceId()).orElseThrow();

    assertThat(loanUpdate)
        .extracting("patronId","bookInstanceId", "status")
        .containsExactly(patron.getId(), bookInstance.getId(), response.status());

    assertEquals(patron.getId(), response.patronId());
    assertEquals(bookInstance.getId(), response.bookInstanceId());
    assertEquals(loanUpdate.getStatus(), response.status());
    assertEquals(
        loanUpdate.getReservedAt().truncatedTo(ChronoUnit.MILLIS),
        response.reservedAt().truncatedTo(ChronoUnit.MILLIS)
    );
    assertEquals(LoanStatus.CANCELLED, loanUpdate.getStatus());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstanceUpdate.getStatus());
  }

  @Test
  void shouldReturnNotFoundWhenLoanIdNotFoundInLoanReserveCancel() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    UUID loanId = UUID.randomUUID();
    ErrorResponse response = returnNotFoundWhenCancelReserve(loanId);

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Loan not found: " + loanId, response.errors().getFirst().description());
  }

  @Test
  void shouldReturnNotFoundWhenBookInstanceIdNotFoundInLoanReserveCancel() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstanceNotSaved = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    Loan loan = loanTestFactory.createLoanReserve(patron, bookInstanceNotSaved);
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenCancelReserve(loan.getId());

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found: " + loan.getBookInstanceId(),
        response.errors().getFirst().description());
  }

  @MethodSource("provideUnavailableLoanStatusForCancelOrCheckout")
  @ParameterizedTest
  void shouldReturnConflictWhenLoanIsNotAvailableToCancel(LoanStatus loanStatus) throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance = bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = loanTestFactory.createLoan(
        UUID.randomUUID(),
        patron.getId(),
        bookInstance.getId(),
        loanStatus,
        null, null, null, null);
    flushAndClear();

    ErrorResponse response = returnConflictWhenCancelReserve(loan.getId());

    assertEquals("409 CONFLICT", response.code());
    assertEquals("The loan is not reserved for cancelled!", response.errors().getFirst().description());
  }

  @Test
  void shouldCreateLoanCheckout() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");

    LoanResponse response = returnIsCreateWhenCreateCheckout();
    flushAndClear();

    Loan loan = loanRepository.findById(response.id()).orElseThrow();
    BookInstance bookInstanceUpdate =
        bookInstanceLoanRepository.findById(response.bookInstanceId()).orElseThrow();

    assertThat(loan)
        .extracting("patronId","bookInstanceId", "status", "lentAt")
        .containsExactly(patron.getId(), bookInstance.getId(), loan.getStatus(), loan.getLentAt());

    assertEquals(patron.getId(), response.patronId());
    assertEquals(bookInstance.getId(), response.bookInstanceId());
    assertEquals(loan.getStatus(), response.status());
    assertEquals(
        loan.getLentAt().truncatedTo(ChronoUnit.MILLIS),
        response.lentAt().truncatedTo(ChronoUnit.MILLIS)
    );
    assertEquals(LoanStatus.LENT, loan.getStatus());
    assertEquals(BookInstanceStatus.LENT, bookInstanceUpdate.getStatus());

    assertEquals(initialCount + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
  }

  @Test
  void shouldReturnNotFoundWhenPatronIdNotFoundInLoanCheckoutCreation() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenCreateCheckout();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Patron not found: " + DEFAULT_PATRON_ID, response.errors().getFirst().description());
  }

  @Test
  void shouldReturnNotFoundWhenBookInstanceIdNotFoundInLoanCheckoutCreation() throws Exception {
    patronTestFactory.createDefaultPatron(PatronType.REGULAR);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenCreateCheckout();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found: " + DEFAULT_BOOK_INSTANCE_ID, response.errors().getFirst().description());
  }

  @Test
  void shouldReturnConflictWhenLoanLimitExceededInLoanCheckoutCreation() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);

    BookInstance bookInstance1 = bookInstanceTestFactory.createBookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    BookInstance bookInstance2 = bookInstanceTestFactory.createBookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    BookInstance bookInstance3 = bookInstanceTestFactory.createBookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    BookInstance bookInstance4 = bookInstanceTestFactory.createDefaultBookInstance(
        BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    loanTestFactory.createLoanReserve(patron, bookInstance1);
    loanTestFactory.createLoanReserve(patron, bookInstance2);
    loanTestFactory.createLoanReserve(patron, bookInstance3);

    entityManager.flush();
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    entityManager.clear();

    ErrorResponse response = returnConflictWhenCreateCheckout();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("409 CONFLICT", response.code());
    assertEquals("Loan Limit Exceeded.", response.errors().getFirst().description());

    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance4.getStatus());
  }

  @Test
  void shouldReturnConflictWhenOnlyResearcherCanLoanRestrictedBooksInCreateLoanCheckout() throws Exception {
    patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance = bookInstanceTestFactory.createDefaultBookInstance(BookType.RESTRICTED, BookInstanceStatus.AVAILABLE);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnConflictWhenCreateCheckout();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("409 CONFLICT", response.code());
    assertEquals("Only Researcher Can Loan Restricted Books!", response.errors().getFirst().description());

    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @MethodSource("provideUnavailableReservedCheckoutStatus")
  @ParameterizedTest
  void shouldReturnConflictWhenBookInstanceNotAvailableExceptionIsThrownInCreateLoanCheckout(BookInstanceStatus bookInstanceStatus) throws Exception {
    patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance = bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, bookInstanceStatus);

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnConflictWhenCreateCheckout();

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("409 CONFLICT", response.code());
    assertEquals("Book Already Unavailable!", response.errors().getFirst().description());

    assertEquals(bookInstanceStatus, bookInstance.getStatus());
  }

  @Test
  void shouldCreateLoanCheckoutFromReserve() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    Loan loan = loanTestFactory.createLoanReserve(patron, bookInstance);
    entityManager.flush();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");

    LoanResponse response = returnIsCreateWhenCreateCheckoutFromReserve(loan.getId());
    flushAndClear();

    Loan loanUpdate = loanRepository.findById(response.id()).orElseThrow();
    BookInstance bookInstanceUpdate =
        bookInstanceLoanRepository.findById(response.bookInstanceId()).orElseThrow();

    assertThat(loanUpdate)
        .extracting("patronId","bookInstanceId", "status")
        .containsExactly(patron.getId(), bookInstance.getId(), loanUpdate.getStatus());

    assertEquals(patron.getId(), response.patronId());
    assertEquals(bookInstance.getId(), response.bookInstanceId());
    assertEquals(loanUpdate.getStatus(), response.status());
    assertEquals(
        loanUpdate.getLentAt().truncatedTo(ChronoUnit.MILLIS),
        response.lentAt().truncatedTo(ChronoUnit.MILLIS)
    );
    assertEquals(LoanStatus.LENT, loanUpdate.getStatus());
    assertEquals(BookInstanceStatus.LENT, bookInstanceUpdate.getStatus());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
  }

  @Test
  void shouldReturnNotFoundWhenLoanIdNotFoundInLoanCheckoutFromReserve() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    UUID loanId = UUID.randomUUID();
    ErrorResponse response = returnNotFoundWhenCreateCheckoutFromReserve(loanId);

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Loan not found: " + loanId, response.errors().getFirst().description());
  }

  @Test
  void shouldReturnNotFoundWhenPatronIdNotFoundInLoanCheckoutFromReserve() throws Exception {
    Patron patron = new Patron(DEFAULT_PATRON_ID, PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    Loan loan = loanTestFactory.createLoanReserve(patron, bookInstance);
    entityManager.flush();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenCreateCheckoutFromReserve(loan.getId());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Patron not found: " + DEFAULT_PATRON_ID, response.errors().getFirst().description());
  }


  @Test
  void shouldReturnNotFoundWhenBookInstanceIdNotFoundInLoanCheckoutFromReserve() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstanceNotSaved = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    Loan loan = loanTestFactory.createLoanReserve(patron, bookInstanceNotSaved);
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenCreateCheckoutFromReserve(loan.getId());

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found: " + loan.getBookInstanceId(),
        response.errors().getFirst().description());
  }

  @MethodSource("provideUnavailableLoanStatusForCancelOrCheckout")
  @ParameterizedTest
  void shouldReturnConflictWhenLoanIsNotAvailableToCheckoutFromReverse(LoanStatus loanStatus) throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance = bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = loanTestFactory.createLoan(
        UUID.randomUUID(),
        patron.getId(),
        bookInstance.getId(),
        loanStatus,
        null, null, null, null);
    flushAndClear();

    ErrorResponse response = returnConflictWhenCheckoutFromReserve(loan.getId());

    assertEquals("409 CONFLICT", response.code());
    assertEquals("You cannot change from status " + loanStatus + " to status LENT", response.errors().getFirst().description());
  }

  @Test
  void shouldReturnLoan() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.LENT);
    Loan loan = loanTestFactory.createLoanLent(patron, bookInstance);
    entityManager.flush();

    LoanResponse response = returnOkWhenReturnLoan(loan.getId());
    flushAndClear();

    Loan loanUpdate = loanRepository.findById(response.id()).orElseThrow();
    BookInstance bookInstanceUpdate =
        bookInstanceLoanRepository.findById(response.bookInstanceId()).orElseThrow();

    assertThat(loanUpdate)
        .extracting("patronId","bookInstanceId", "status")
        .containsExactly(patron.getId(), bookInstance.getId(), response.status());

    assertEquals(patron.getId(), response.patronId());
    assertEquals(bookInstance.getId(), response.bookInstanceId());
    assertEquals(loanUpdate.getStatus(), response.status());
    assertEquals(
        loanUpdate.getReturnedAt().truncatedTo(ChronoUnit.MILLIS),
        response.returnedAt().truncatedTo(ChronoUnit.MILLIS)
    );
    assertEquals(LoanStatus.RETURNED, loanUpdate.getStatus());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstanceUpdate.getStatus());
  }

  @Test
  void shouldReturnLoanDelayed() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.LENT);
    Loan loan = loanTestFactory.createLoan(
        UUID.randomUUID(),
        patron.getId(),
        bookInstance.getId(),
        LoanStatus.DELAYED,
        null, null, null, null
    );
    entityManager.flush();

    LoanResponse response = returnOkWhenReturnLoan(loan.getId());
    flushAndClear();

    Loan loanUpdate = loanRepository.findById(response.id()).orElseThrow();
    BookInstance bookInstanceUpdate =
        bookInstanceLoanRepository.findById(response.bookInstanceId()).orElseThrow();

    assertThat(loanUpdate)
        .extracting("patronId","bookInstanceId", "status")
        .containsExactly(patron.getId(), bookInstance.getId(), response.status());

    assertEquals(patron.getId(), response.patronId());
    assertEquals(bookInstance.getId(), response.bookInstanceId());
    assertEquals(loanUpdate.getStatus(), response.status());
    assertEquals(
        loanUpdate.getReturnedAt().truncatedTo(ChronoUnit.MILLIS),
        response.returnedAt().truncatedTo(ChronoUnit.MILLIS)
    );
    assertEquals(LoanStatus.RETURNED_WITH_DELAY, loanUpdate.getStatus());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstanceUpdate.getStatus());
  }

  @Test
  void shouldReturnNotFoundWhenLoanIdNotFoundInLoanReturn() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan");
    flushAndClear();

    UUID loanId = UUID.randomUUID();
    ErrorResponse response = returnNotFoundWhenReturnLoan(loanId);

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "loan"));
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Loan not found: " + loanId, response.errors().getFirst().description());
  }

  @Test
  void shouldReturnNotFoundWhenBookInstanceIdNotFoundInLoanReturn() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstanceNotSaved = new BookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    Loan loan = loanTestFactory.createLoanReserve(patron, bookInstanceNotSaved);
    flushAndClear();

    ErrorResponse response = returnNotFoundWhenReturnLoan(loan.getId());

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found: " + loan.getBookInstanceId(),
        response.errors().getFirst().description());
  }

  @MethodSource("provideUnavailableLoanStatusForReturn")
  @ParameterizedTest
  void shouldReturnConflictWhenLoanIsNotAvailableToReturn(LoanStatus loanStatus) throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance = bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    Loan loan = loanTestFactory.createLoan(
        UUID.randomUUID(),
        patron.getId(),
        bookInstance.getId(),
        loanStatus,
        null, null, null, null);
    flushAndClear();

    ErrorResponse response = returnConflictWhenReturnLoan(loan.getId());

    assertEquals("409 CONFLICT", response.code());
    assertEquals("The loan is not lent or delayed for returned!", response.errors().getFirst().description());
  }

  @Test
  void shouldGetById() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance =
        bookInstanceTestFactory.createDefaultBookInstance(BookType.CIRCULATING, BookInstanceStatus.LENT);
    Loan loan = loanTestFactory.createLoanLent(patron, bookInstance);
    flushAndClear();

    LoanResponse response = returnOkWhenGetLoanById(loan.getId());

    assertEquals(loan.getId(), response.id());
    assertEquals(loan.getPatronId(), response.patronId());
    assertEquals(loan.getBookInstanceId(), response.bookInstanceId());
    assertEquals(loan.getStatus(), response.status());
    assertEquals(loan.getLentAt().truncatedTo(ChronoUnit.MILLIS), response.lentAt().truncatedTo(ChronoUnit.MILLIS));
  }

  @Test
  void shouldReturnNotFoundWhenLoanIdNotFoundInGetById() throws Exception {
    UUID loanId = UUID.randomUUID();
    ErrorResponse response = returnNotFoundWhenGetLoanById(loanId);

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Loan not found: " + loanId, response.errors().getFirst().description());
  }

  @Test
  void shouldGetAllLoansByPatronId() throws Exception {
    Patron patron = patronTestFactory.createDefaultPatron(PatronType.REGULAR);
    BookInstance bookInstance1 = bookInstanceTestFactory.createDefaultBookInstance(
        BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    BookInstance bookInstance2 = bookInstanceTestFactory.createBookInstance(
        UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    Loan loan1 = loanTestFactory.createLoanLent(patron, bookInstance1);
    Loan loan2 = loanTestFactory.createLoanLent(patron, bookInstance2);
    flushAndClear();

    List<LoanResponse> response = returnOkWhenGetAllLoansByPatronId(patron.getId());

    assertEquals(2, response.size());
    assertEquals(loan1.getId(), response.getFirst().id());
    assertEquals(loan1.getPatronId(), response.getFirst().patronId());
    assertEquals(loan1.getBookInstanceId(), response.getFirst().bookInstanceId());
    assertEquals(loan1.getStatus(), response.getFirst().status());
    assertEquals(loan1.getLentAt().truncatedTo(ChronoUnit.MILLIS), response.getFirst().lentAt().truncatedTo(ChronoUnit.MILLIS));
    assertEquals(loan2.getId(), response.get(1).id());
    assertEquals(loan2.getPatronId(), response.get(1).patronId());
    assertEquals(loan2.getBookInstanceId(), response.get(1).bookInstanceId());
    assertEquals(loan2.getStatus(), response.get(1).status());
    assertEquals(loan2.getLentAt().truncatedTo(ChronoUnit.MILLIS), response.get(1).lentAt().truncatedTo(ChronoUnit.MILLIS));
  }
}
