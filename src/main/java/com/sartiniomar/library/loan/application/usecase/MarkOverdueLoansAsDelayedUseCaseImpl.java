package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.MarkOverdueLoansAsDelayedUseCase;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.Loan;
import java.util.List;

public class MarkOverdueLoansAsDelayedUseCaseImpl implements MarkOverdueLoansAsDelayedUseCase {

  private final LoanRepository loanRepository;

  public MarkOverdueLoansAsDelayedUseCaseImpl(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @Override
  public void execute() {
    List<Loan> loans = loanRepository.findLoansDue();
    for (Loan loan : loans) {
      loan.delayed();
      loanRepository.save(loan);
    }
  }
}
