package com.sartiniomar.library.lending.domain.hold;

import com.sartiniomar.library.lending.domain.bookInstance.BookInstance;
import com.sartiniomar.library.lending.domain.patron.Patron;
import java.util.List;

public class PlacingOnHoldService {

  public DomainResult<Hold> placeOnHold(Patron patron, BookInstance book) {

    book.ensureCanBePlacedOnHoldBy(patron);

    Hold hold = new Hold(
        patron.getId(),
        book.getId()
    );

    BookPlacedOnHoldEvent event =
        new BookPlacedOnHoldEvent(patron.getId(), book.getId());

    return new DomainResult<>(hold, List.of(event));
  }
}
