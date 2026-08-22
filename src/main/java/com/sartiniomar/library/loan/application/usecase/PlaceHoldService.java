package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.loan.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.PlacingOnHoldService;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.loan.domain.loan.LoanLimitExceededException;
import org.springframework.transaction.annotation.Transactional;

public class PlaceHoldService implements PlaceHoldUseCase {

  private final PatronLoanRepository patronRepository;
  private final BookInstanceLoanRepository bookInstanceLendingRepository;
  private final LoanRepository holdRepository;
  private final DomainEventPublisher eventPublisher;
  private final PlacingOnHoldService domainService;

  public PlaceHoldService(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      DomainEventPublisher eventPublisher,
      PlacingOnHoldService domainService
  ) {
    this.patronRepository = patronLoanRepository;
    this.bookInstanceLendingRepository = bookInstanceLoanRepository;
    this.holdRepository = loanRepository;
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

    int currentHolds = holdRepository.countByPatronId(patron.getId());

    if (currentHolds >= patron.maxLoans()) {
      throw new LoanLimitExceededException("Hold Limit Exceeded");
    }

    DomainResult<Loan> result = domainService.placeOnHold(patron, book);

    holdRepository.save(result.result());

    result.events().forEach(eventPublisher::publish);

    return result.result();
  }
}