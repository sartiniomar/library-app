package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.checkout.CheckoutCommand;
import com.sartiniomar.library.loan.application.port.in.checkout.CheckoutUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.application.service.LoanLimitChecker;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutServiceDomain;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class CheckoutUseCaseImpl implements CheckoutUseCase {

  private final PatronLoanRepository patronRepository;
  private final BookInstanceLoanRepository bookInstanceRepository;
  private final LoanRepository loanRepository;
  private final CheckoutServiceDomain domainService;
  private final LoanLimitChecker validationsUtil;

  public CheckoutUseCaseImpl(PatronLoanRepository patronRepository, BookInstanceLoanRepository bookInstanceRepository, LoanRepository loanRepository, CheckoutServiceDomain domainService, LoanLimitChecker validationsUtil) {
    this.patronRepository = patronRepository;
    this.bookInstanceRepository = bookInstanceRepository;
    this.loanRepository = loanRepository;
    this.domainService = domainService;
    this.validationsUtil = validationsUtil;
  }

  @Override
  @Transactional
  public Loan execute(CheckoutCommand command) {
    Patron patron = patronRepository.findById(command.patronId())
        .orElseThrow(() -> new PatronNotFoundException(command.patronId().toString()));

    BookInstance bookInstance = bookInstanceRepository.findById(command.bookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException(command.bookInstanceId().toString()));

    validationsUtil.check(patron);

    Loan result = domainService.checkout(patron, bookInstance);

    loanRepository.save(result);

    return result;
  }
}
