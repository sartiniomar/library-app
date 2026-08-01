package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.application.port.out.*;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookInstanceNotFoundException;
import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import com.sartiniomar.library.lending.domain.hold.Hold;
import com.sartiniomar.library.lending.domain.hold.DomainResult;
import com.sartiniomar.library.lending.domain.patron.Patron;
import com.sartiniomar.library.lending.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.lending.domain.hold.HoldLimitExceededException;
import org.springframework.transaction.annotation.Transactional;

public class PlaceHoldService implements PlaceHoldUseCase {

  private final PatronLendingRepository patronRepository;
  private final BookInstanceRepository bookInstanceRepository;
  private final HoldRepository holdRepository;
  private final DomainEventPublisher eventPublisher;
  private final PlacingOnHoldService domainService;

  public PlaceHoldService(
      PatronLendingRepository patronRepository,
      BookInstanceRepository bookInstanceRepository,
      HoldRepository holdRepository,
      DomainEventPublisher eventPublisher,
      PlacingOnHoldService domainService
  ) {
    this.patronRepository = patronRepository;
    this.bookInstanceRepository = bookInstanceRepository;
    this.holdRepository = holdRepository;
    this.eventPublisher = eventPublisher;
    this.domainService = domainService;
  }

  @Override
  @Transactional
  public Hold execute(PlaceHoldCommand command) {

    Patron patron = patronRepository.findById(command.patronId())
        .orElseThrow(() -> new PatronNotFoundException(command.patronId().toString()));

    BookInstance book = bookInstanceRepository.findById(command.bookInstanceId())
        .orElseThrow(() -> new BookInstanceNotFoundException("UUID=" + command.bookInstanceId()));

    int currentHolds = holdRepository.countByPatronId(patron.getId());

    if (currentHolds >= patron.maxHolds()) {
      throw new HoldLimitExceededException("Hold Limit Exceeded");
    }

    bookInstanceRepository.save(book);

    DomainResult<Hold> result = domainService.placeOnHold(patron, book);

    holdRepository.save(result.result());

    result.events().forEach(eventPublisher::publish);

    return result.result();
  }
}