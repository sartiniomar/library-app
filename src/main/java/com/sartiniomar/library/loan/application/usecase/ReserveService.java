package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.loan.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class ReserveService implements PlaceHoldUseCase {

  private final PatronLoanRepository patronRepository;
  private final BookInstanceLoanRepository bookInstanceLendingRepository;
  private final LoanRepository loanRepository;
  private final DomainEventPublisher eventPublisher;
  private final ReserveServiceDomain domainService;

  public ReserveService(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      DomainEventPublisher eventPublisher,
      ReserveServiceDomain domainService
  ) {
    this.patronRepository = patronLoanRepository;
    this.bookInstanceLendingRepository = bookInstanceLoanRepository;
    this.loanRepository = loanRepository;
    this.eventPublisher = eventPublisher;
    this.domainService = domainService;
  }

  @Override
  @Transactional
  public Loan execute(PlaceHoldCommand command) {
    Patron patron = patronRepository.findById(command.patronId())
        .orElseThrow(() -> new PatronNotFoundException(command.patronId().toString()));

    BookInstance book = bookInstanceLendingRepository.findById(command.bookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException("UUID=" + command.bookInstanceId()));

    this.validations(patron);

    DomainResult<Loan> result = domainService.reserve(patron, book);

    loanRepository.save(result.result());

    result.events().forEach(eventPublisher::publish);

    return result.result();
  }

  private void validations(Patron patron) {
    if (patron.isRegular()) {
      int activeLoans = loanRepository.countActiveLoansByPatronId(patron.getId(), DomainPolicy.ACTIVE_STATUSES);
      DomainPolicy.ensureCanHaveAnotherLoan(patron, activeLoans);
    }
  }
}