package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.domain.bookInstance.BookInstance;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.application.port.out.*;
import com.sartiniomar.library.lending.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import com.sartiniomar.library.lending.domain.hold.Hold;
import com.sartiniomar.library.lending.domain.hold.DomainResult;
import com.sartiniomar.library.lending.domain.patron.Patron;
import com.sartiniomar.library.lending.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.lending.domain.hold.HoldLimitExceededException;
import org.springframework.transaction.annotation.Transactional;

public class PlaceHoldService implements PlaceHoldUseCase {

  private final PatronLendingRepository patronRepository;
  private final BookInstanceLendingRepository bookInstanceLendingRepository;
  private final HoldRepository holdRepository;
  private final DomainEventPublisher eventPublisher;
  private final PlacingOnHoldService domainService;

  public PlaceHoldService(
      PatronLendingRepository patronRepository,
      BookInstanceLendingRepository bookInstanceLendingRepository,
      HoldRepository holdRepository,
      DomainEventPublisher eventPublisher,
      PlacingOnHoldService domainService
  ) {
    this.patronRepository = patronRepository;
    this.bookInstanceLendingRepository = bookInstanceLendingRepository;
    this.holdRepository = holdRepository;
    this.eventPublisher = eventPublisher;
    this.domainService = domainService;
  }

  @Override
  @Transactional
  public Hold execute(PlaceHoldCommand command) {

    Patron patron = patronRepository.findById(command.patronId())
        .orElseThrow(() -> new PatronNotFoundException(command.patronId().toString()));

    BookInstance book = bookInstanceLendingRepository.findById(command.bookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException("UUID=" + command.bookInstanceId()));

    int currentHolds = holdRepository.countByPatronId(patron.getId());

    if (currentHolds >= patron.maxHolds()) {
      throw new HoldLimitExceededException("Hold Limit Exceeded");
    }

    DomainResult<Hold> result = domainService.placeOnHold(patron, book);

    holdRepository.save(result.result());

    result.events().forEach(eventPublisher::publish);

    return result.result();
  }
}