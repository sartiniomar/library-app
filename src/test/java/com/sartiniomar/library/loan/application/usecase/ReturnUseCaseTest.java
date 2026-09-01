package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.LoanIdCommand;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanNotFoundException;
import com.sartiniomar.library.loan.domain.loan.TransitionStatusException;
import com.sartiniomar.library.loan.domain.loan.service.ReturnServiceDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ReturnUseCaseTest {

  @Mock
  private BookInstanceLoanRepository bookInstanceRepository;

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private ReturnServiceDomain serviceDomain;

  @InjectMocks
  private ReturnUseCaseImpl useCase;

  @Test
  void shouldExecuteReturnSuccessfully() {
    Clock clock = Clock.systemDefaultZone();

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.LENT
    );

    Loan loan = Loan.createLent(UUID.randomUUID(), bookInstance.getId(), clock, 7);

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    when(serviceDomain.returned(loan, bookInstance))
        .thenReturn(loan);

    when(loanRepository.save(loan))
        .thenReturn(loan);

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    Loan result = useCase.execute(command);

    assertNotNull(result);
    assertEquals(loan, result);

    verify(loanRepository).findById(loan.getId());
    verify(bookInstanceRepository).findById(bookInstance.getId());
    verify(serviceDomain).returned(loan, bookInstance);
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
  void should_throw_exception_when_bookInstance_not_exist() {
    Clock clock = Clock.systemDefaultZone();

    Loan loan = Loan.createReserve(UUID.randomUUID(), UUID.randomUUID(), clock);

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    BookInstanceNotFoundException ex =
        assertThrows(BookInstanceNotFoundException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Book Instance not found: " + loan.getBookInstanceId(), ex.getMessage());
  }

  @Test
  void should_not_persist_loan_when_domain_service_fails() {
    Clock clock = Clock.systemDefaultZone();

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.AVAILABLE
    );

    Loan loan = Loan.createReserve(UUID.randomUUID(), bookInstance.getId(), clock);

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    when(bookInstanceRepository.findById(bookInstance.getId()))
        .thenReturn(Optional.of(bookInstance));

    TransitionStatusException exception =
        new TransitionStatusException("Transition Status Error");

    when(serviceDomain.returned(loan, bookInstance))
        .thenThrow(exception);

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    TransitionStatusException ex =
        assertThrows(
            TransitionStatusException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Transition Status Error", ex.getMessage());

    verify(serviceDomain).returned(loan, bookInstance);
    verify(loanRepository, never()).save(any(Loan.class));
  }
}
