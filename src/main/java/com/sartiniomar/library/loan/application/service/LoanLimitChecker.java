package com.sartiniomar.library.loan.application.service;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.patron.Patron;

public class LoanLimitChecker {

  private final LoanRepository loanRepository;

  public LoanLimitChecker(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  public void check(Patron patron) {
    if (patron.isRegular()) {
      int activeLoans = loanRepository.countActiveLoansByPatronId(patron.getId(), DomainPolicy.ACTIVE_STATUSES);
      DomainPolicy.ensureCanHaveAnotherLoan(patron, activeLoans);
    }
  }
}
