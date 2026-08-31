package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.checkout.CheckoutReserveCommand;
import com.sartiniomar.library.loan.application.port.in.checkout.CheckoutReserveUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanNotFoundException;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutReserveServiceDomain;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class CheckoutReserveUseCaseImpl implements CheckoutReserveUseCase {

  private final LoanRepository loanRepository;
  private final PatronLoanRepository patronRepository;
  private final BookInstanceLoanRepository bookInstanceRepository;
  private final CheckoutReserveServiceDomain domainService;

  public CheckoutReserveUseCaseImpl(LoanRepository loanRepository, PatronLoanRepository patronRepository, BookInstanceLoanRepository bookInstanceRepository, CheckoutReserveServiceDomain domainService) {
    this.loanRepository = loanRepository;
    this.patronRepository = patronRepository;
    this.bookInstanceRepository = bookInstanceRepository;
    this.domainService = domainService;
  }

  @Override
  @Transactional
  public Loan execute(CheckoutReserveCommand command) {
    Loan loan = loanRepository.findById(command.loanId())
        .orElseThrow(() -> new LoanNotFoundException(command.loanId().toString()));

    Patron patron = patronRepository.findById(loan.getPatronId())
        .orElseThrow(() -> new PatronNotFoundException(loan.getPatronId().toString()));

    BookInstance bookInstance = bookInstanceRepository.findById(loan.getBookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException(loan.getBookInstanceId().toString()));

    Loan result = domainService.checkoutReserve(loan, patron, bookInstance);

    loanRepository.save(result);

    return result;
  }
}
