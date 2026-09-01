package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.LoanIdCommand;
import com.sartiniomar.library.loan.application.port.in.ReturnUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanNotFoundException;
import com.sartiniomar.library.loan.domain.loan.service.ReturnServiceDomain;
import org.springframework.transaction.annotation.Transactional;

public class ReturnUseCaseImpl implements ReturnUseCase {

  private final LoanRepository loanRepository;
  private final BookInstanceLoanRepository bookInstanceRepository;
  private final ReturnServiceDomain domainService;

  public ReturnUseCaseImpl(LoanRepository loanRepository, BookInstanceLoanRepository bookInstanceRepository, ReturnServiceDomain domainService) {
    this.loanRepository = loanRepository;
    this.bookInstanceRepository = bookInstanceRepository;
    this.domainService = domainService;
  }

  @Override
  @Transactional
  public Loan execute(LoanIdCommand command) {
    Loan loan = loanRepository.findById(command.loanId())
        .orElseThrow(() -> new LoanNotFoundException(command.loanId().toString()));

    BookInstance bookInstance = bookInstanceRepository.findById(loan.getBookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException(loan.getBookInstanceId().toString()));

    Loan result = domainService.returned(loan, bookInstance);

    loanRepository.save(result);

    return result;
  }
}
