package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.reserveCommand;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class ReserveService implements ReserveUseCase {

  private final PatronLoanRepository patronRepository;
  private final BookInstanceLoanRepository bookInstanceRepository;
  private final LoanRepository loanRepository;
  private final ReserveServiceDomain domainService;

  public ReserveService(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      ReserveServiceDomain domainService
  ) {
    this.patronRepository = patronLoanRepository;
    this.bookInstanceRepository = bookInstanceLoanRepository;
    this.loanRepository = loanRepository;
    this.domainService = domainService;
  }

  @Override
  @Transactional
  public Loan execute(reserveCommand command) {
    Patron patron = patronRepository.findById(command.patronId())
        .orElseThrow(() -> new PatronNotFoundException(command.patronId().toString()));

    BookInstance book = bookInstanceRepository.findById(command.bookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException(command.bookInstanceId().toString()));

    this.validations(patron);

    Loan result = domainService.reserve(patron, book);

    loanRepository.save(result);

    return result;
  }

  private void validations(Patron patron) {
    if (patron.isRegular()) {
      int activeLoans = loanRepository.countActiveLoansByPatronId(patron.getId(), DomainPolicy.ACTIVE_STATUSES);
      DomainPolicy.ensureCanHaveAnotherLoan(patron, activeLoans);
    }
  }
}