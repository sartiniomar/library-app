package com.sartiniomar.library.loan.application.service;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanLimitExceededException;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class LoanLimitCheckerTest {

  @Mock
  private LoanRepository loanRepository;

  @InjectMocks
  private LoanLimitChecker validationsUtil;

  @Test
  void should_throw_exception_when_regular_patron_reached_loan_limit() {
    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    when(loanRepository.countActiveLoansByPatronId(
        patron.getId(),
        Loan.ACTIVE_STATUSES
    )).thenReturn(DomainPolicy.MAX_VALUE_LOANS_REGULAR_PATRON);

    LoanLimitExceededException ex = assertThrows(
        LoanLimitExceededException.class,
        () -> validationsUtil.check(patron)
    );

    assertEquals("Loan Limit Exceeded.", ex.getMessage());

    verify(loanRepository).countActiveLoansByPatronId(
        patron.getId(),
        Loan.ACTIVE_STATUSES
    );
  }

  @Test
  void should_validate_successfully_when_regular_patron_is_below_loan_limit() {
    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.REGULAR
    );

    when(loanRepository.countActiveLoansByPatronId(
        patron.getId(),
        Loan.ACTIVE_STATUSES
    )).thenReturn(DomainPolicy.MAX_VALUE_LOANS_REGULAR_PATRON - 1);

    assertDoesNotThrow(() -> validationsUtil.check(patron));

    verify(loanRepository).countActiveLoansByPatronId(
        patron.getId(),
        Loan.ACTIVE_STATUSES
    );
  }

  @Test
  void should_not_check_active_loans_when_patron_is_researcher() {
    Patron patron = new Patron(
        UUID.randomUUID(),
        PatronType.RESEARCHER
    );

    assertDoesNotThrow(
        () -> validationsUtil.check(patron)
    );

    verify(loanRepository, never())
        .countActiveLoansByPatronId(any(), any());
  }
}
