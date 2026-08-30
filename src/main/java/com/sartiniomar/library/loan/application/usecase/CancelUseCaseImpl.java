package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.cancel.CancelCommand;
import com.sartiniomar.library.loan.application.port.in.cancel.CancelUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanNotFoundException;
import com.sartiniomar.library.loan.domain.loan.service.CancelServiceDomain;

public class CancelUseCaseImpl implements CancelUseCase {

  private final CancelServiceDomain domainService;
  private final LoanRepository loanRepository;
  private final BookInstanceLoanRepository bookInstanceRepository;

  public CancelUseCaseImpl(CancelServiceDomain domainService, LoanRepository loanRepository, BookInstanceLoanRepository bookInstanceRepository) {
    this.domainService = domainService;
    this.loanRepository = loanRepository;
    this.bookInstanceRepository = bookInstanceRepository;
  }

  @Override
  public Loan execute(CancelCommand command) {
    Loan loan = loanRepository.findById(command.loanId())
        .orElseThrow(() -> new LoanNotFoundException(command.loanId().toString()));

    BookInstance bookInstance = bookInstanceRepository.findById(loan.getBookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException(loan.getBookInstanceId().toString()));

    Loan result = domainService.cancel(loan, bookInstance);

    loanRepository.save(result);

    return result;
  }
}
