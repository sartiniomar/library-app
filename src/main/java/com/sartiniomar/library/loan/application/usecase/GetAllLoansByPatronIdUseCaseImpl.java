package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.GetAllLoansByPatronIdUseCase;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.Loan;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;

@Slf4j
public class GetAllLoansByPatronIdUseCaseImpl implements GetAllLoansByPatronIdUseCase {

  private final LoanRepository loanRepository;

  public GetAllLoansByPatronIdUseCaseImpl(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @Override
  public List<Loan> execute(UUID patronId) {
    log.debug("Finding loans. Patron Id= {}", patronId);

    List<Loan> loans = loanRepository.findAllByPatronId(patronId);

    log.debug("Loans found. Patron Id= {}", patronId);
    return loans;
  }
}
