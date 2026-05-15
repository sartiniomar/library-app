package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.application.port.out.*;
import com.sartiniomar.library.lending.model.book.BookInstance;
import com.sartiniomar.library.lending.model.book.BookNotFoundException;
import com.sartiniomar.library.lending.model.hold.PlacingOnHoldService;
import com.sartiniomar.library.lending.model.hold.Hold;
import com.sartiniomar.library.lending.model.hold.DomainResult;
import com.sartiniomar.library.lending.model.patron.Patron;
import com.sartiniomar.library.lending.model.patron.PatronNotFoundException;
import com.sartiniomar.library.lending.model.patron.HoldLimitExceededException;
import org.springframework.transaction.annotation.Transactional;

public class PlaceHoldService implements PlaceHoldUseCase {

  private final PatronRepository patronRepository;
  private final BookInstanceRepository bookInstanceRepository;
  private final HoldRepository holdRepository;
  private final DomainEventPublisher eventPublisher;
  private final PlacingOnHoldService domainService;

  public PlaceHoldService(
      PatronRepository patronRepository,
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
  public void execute(PlaceHoldCommand command) {

    Patron patron = patronRepository.findById(command.getPatronId())
        .orElseThrow(() -> new PatronNotFoundException(command.getPatronId().toString()));

    BookInstance book = bookInstanceRepository.findById(command.getBookId())
        .orElseThrow(() -> new BookNotFoundException(command.getBookId()));

    int currentHolds = holdRepository.countByPatronId(patron.getId());

    if (currentHolds >= patron.maxHolds()) {
      throw new HoldLimitExceededException("Hold Limit Exceeded");
    }

    // 🔥 persistimos estado del agregado (ACTIVA @Version)
    bookInstanceRepository.save(book);

    DomainResult<Hold> result = domainService.placeOnHold(patron, book);

    holdRepository.save(result.result());

    result.events().forEach(eventPublisher::publish);
  }
}