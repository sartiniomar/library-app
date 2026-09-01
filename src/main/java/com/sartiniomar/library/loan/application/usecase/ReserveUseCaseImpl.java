package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.application.service.LoanLimitChecker;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class ReserveUseCaseImpl implements ReserveUseCase {

  private final PatronLoanRepository patronRepository;
  private final BookInstanceLoanRepository bookInstanceRepository;
  private final LoanRepository loanRepository;
  private final ReserveServiceDomain domainService;
  private final LoanLimitChecker validationsUtil;

  public ReserveUseCaseImpl(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      ReserveServiceDomain domainService, LoanLimitChecker validationsUtil
  ) {
    this.patronRepository = patronLoanRepository;
    this.bookInstanceRepository = bookInstanceLoanRepository;
    this.loanRepository = loanRepository;
    this.domainService = domainService;
    this.validationsUtil = validationsUtil;
  }

  @Override
  @Transactional
  public Loan execute(LoanCommand command) {
    Patron patron = patronRepository.findById(command.patronId())
        .orElseThrow(() -> new PatronNotFoundException(command.patronId().toString()));

    BookInstance bookInstance = bookInstanceRepository.findById(command.bookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException(command.bookInstanceId().toString()));

    validationsUtil.check(patron);

    Loan result = domainService.reserve(patron, bookInstance);

    loanRepository.save(result);

    return result;
  }
}