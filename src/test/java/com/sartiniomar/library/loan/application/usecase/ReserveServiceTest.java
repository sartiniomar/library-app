package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.reserveCommand;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotAvailableException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanLimitExceededException;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ReserveServiceTest {

  @Mock
  private PatronLoanRepository patronRepository;

  @Mock
  private BookInstanceLoanRepository bookInstanceRepository;

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private ReserveServiceDomain reserveService;

  @InjectMocks
  private ReserveService useCase;

  @Test
  void shouldExecuteReserveSuccessfully() {
    Clock clock = Clock.systemDefaultZone();

    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.AVAILABLE
    );

    Loan loan = Loan.createReserve(patron.getId(), bookInstance.getId(), clock);

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    when(reserveService.reserve(patron, bookInstance))
        .thenReturn(loan);

    when(loanRepository.save(loan))
        .thenReturn(loan);

    reserveCommand command = new reserveCommand(patron.getId(), bookInstance.getId());

    Loan result = useCase.execute(command);

    assertNotNull(result);
    assertEquals(loan, result);

    verify(patronRepository).findById(patron.getId());
    verify(bookInstanceRepository).findById(bookInstance.getId());
    verify(reserveService).reserve(patron, bookInstance);
    verify(loanRepository).save(loan);
  }

  @Test
  void should_throw_exception_when_patron_not_exist() {
    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.AVAILABLE
    );

    UUID patronId = UUID.randomUUID();
    reserveCommand command = new reserveCommand(patronId, bookInstance.getId());

    PatronNotFoundException ex =
        assertThrows(PatronNotFoundException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Patron not found: " + patronId, ex.getMessage());
  }

  @Test
  void should_throw_exception_when_bookInstance_not_exist() {
    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    UUID bookInstanceId = UUID.randomUUID();
    reserveCommand command = new reserveCommand(patron.getId(), bookInstanceId);

    BookInstanceNotFoundException ex =
        assertThrows(BookInstanceNotFoundException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Book Instance not found: " + bookInstanceId, ex.getMessage());
  }

  @Test
  void should_throw_exception_when_patron_reached_loan_limit() {
    Clock clock = Clock.systemDefaultZone();

    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.AVAILABLE
    );

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    when(loanRepository.countActiveLoansByPatronId(patron.getId(), DomainPolicy.ACTIVE_STATUSES))
        .thenReturn(DomainPolicy.MAX_VALUE_LOANS_REGULAR_PATRON + 1);

    reserveCommand command = new reserveCommand(patron.getId(), bookInstance.getId());

    LoanLimitExceededException ex =
        assertThrows(LoanLimitExceededException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Loan Limit Exceeded.", ex.getMessage());
  }

  @Test
  void should_not_check_active_loans_when_patron_is_not_regular() {
    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.RESEARCHER
    );

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.AVAILABLE
    );

    Loan loan = Loan.createReserve(
        patron.getId(),
        bookInstance.getId(),
        Clock.systemDefaultZone()
    );

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    when(reserveService.reserve(patron, bookInstance))
        .thenReturn(loan);

    when(loanRepository.save(loan))
        .thenReturn(loan);

    reserveCommand command =
        new reserveCommand(patron.getId(), bookInstance.getId());

    Loan result = useCase.execute(command);

    assertNotNull(result);
    assertEquals(loan, result);

    verify(loanRepository, never()).countActiveLoansByPatronId(patron.getId(), DomainPolicy.ACTIVE_STATUSES);
    verify(loanRepository).save(loan);
  }

  @Test
  void should_not_persist_loan_when_domain_service_fails() {
    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.AVAILABLE
    );

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    BookInstanceNotAvailableException exception =
        new BookInstanceNotAvailableException("Book Already Unavailable!");

    when(reserveService.reserve(patron, bookInstance))
        .thenThrow(exception);

    reserveCommand command =
        new reserveCommand(patron.getId(), bookInstance.getId());

    BookInstanceNotAvailableException ex =
        assertThrows(
            BookInstanceNotAvailableException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Book Already Unavailable!", ex.getMessage());

    verify(reserveService).reserve(patron, bookInstance);
    verify(loanRepository, never()).save(any(Loan.class));
  }
}