package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarkOverdueLoansAsDelayedUseCaseTest {

  @Mock
  private LoanRepository loanRepository;

  @InjectMocks
  private MarkOverdueLoansAsDelayedUseCaseImpl useCase;

  @Test
  void shouldExecuteMArkOverdueLoansSuccessfully() {
    Loan loan1 = Loan.createLent(UUID.randomUUID(), UUID.randomUUID(), Clock.systemUTC(), -1);
    Loan loan2 = Loan.createLent(UUID.randomUUID(), UUID.randomUUID(), Clock.systemUTC(), -1);

    when(loanRepository.findLoansDue()).thenReturn(List.of(loan1, loan2));

    when(loanRepository.save(loan1)).thenReturn(loan1);
    when(loanRepository.save(loan2)).thenReturn(loan2);

    useCase.execute();

    assertEquals(LoanStatus.DELAYED, loan1.getStatus());
    assertEquals(LoanStatus.DELAYED, loan2.getStatus());

    verify(loanRepository).findLoansDue();
    verify(loanRepository).save(loan1);
    verify(loanRepository).save(loan2);
  }
}
