package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.LoanIdCommand;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanNotFoundException;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.domain.loan.TransitionStatusException;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutReserveServiceDomain;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutReserveUseCaseTest {

  @Mock
  private PatronLoanRepository patronRepository;

  @Mock
  private BookInstanceLoanRepository bookInstanceRepository;

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private CheckoutReserveServiceDomain domainService;

  @InjectMocks
  private CheckoutReserveUseCaseImpl useCase;

  @Test
  void shouldExecuteCheckoutReserveSuccessfully() {
    Instant now = Instant.parse("2026-08-27T19:00:00Z");

    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.RESERVED
    );

    Loan loan = new Loan(
        patron.getId(),
        bookInstance.getId(),
        LoanStatus.RESERVED,
        now,
        null,
        now.plus(Duration.ofDays(patron.getLimitDays())),
        null
    );

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    when(domainService.checkoutReserve(loan, patron, bookInstance))
        .thenReturn(loan);

    when(loanRepository.save(loan))
        .thenReturn(loan);

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    Loan result = useCase.execute(command);

    assertNotNull(result);
    assertEquals(loan, result);

    verify(patronRepository).findById(patron.getId());
    verify(bookInstanceRepository).findById(bookInstance.getId());
    verify(loanRepository).findById(loan.getId());
    verify(domainService).checkoutReserve(loan, patron, bookInstance);
    verify(loanRepository).save(loan);
  }

  @Test
  void should_throw_exception_when_loan_not_exist() {
    UUID loanId = UUID.randomUUID();
    LoanIdCommand command = new LoanIdCommand(loanId);

    LoanNotFoundException ex =
        assertThrows(LoanNotFoundException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Loan not found: " + loanId, ex.getMessage());
  }

  @Test
  void should_throw_exception_when_patron_not_exist() {
    UUID patronId = UUID.randomUUID();

    Loan loan = new Loan(
        patronId,
        UUID.randomUUID(),
        LoanStatus.RESERVED,
        Instant.now(),
        null,
        Instant.now().plus(Duration.ofDays(3)),
        null
    );

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    PatronNotFoundException ex =
        assertThrows(PatronNotFoundException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Patron not found: " + patronId, ex.getMessage());
  }

  @Test
  void should_throw_exception_when_book_instance_not_exist() {
    UUID bookInstanceId = UUID.randomUUID();

    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    Loan loan = new Loan(
        patron.getId(),
        bookInstanceId,
        LoanStatus.RESERVED,
        Instant.now(),
        null,
        Instant.now().plus(Duration.ofDays(3)),
        null
    );

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    BookInstanceNotFoundException ex =
        assertThrows(BookInstanceNotFoundException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Book Instance not found: " + bookInstanceId, ex.getMessage());
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

    Loan loan = new Loan(
        patron.getId(),
        bookInstance.getId(),
        LoanStatus.RESERVED,
        Instant.now(),
        null,
        Instant.now().plus(Duration.ofDays(patron.getLimitDays())),
        null
    );

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    when(patronRepository.findById(patron.getId()))
        .thenReturn(Optional.of(patron));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    TransitionStatusException exception =
        new TransitionStatusException("You cant change the status!");

    when(domainService.checkoutReserve(loan, patron, bookInstance))
        .thenThrow(exception);

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    TransitionStatusException ex =
        assertThrows(
            TransitionStatusException.class,
            () -> useCase.execute(command)
        );

    assertEquals("You cant change the status!", ex.getMessage());

    verify(domainService).checkoutReserve(loan, patron, bookInstance);
    verify(loanRepository, never()).save(any(Loan.class));
  }
}
