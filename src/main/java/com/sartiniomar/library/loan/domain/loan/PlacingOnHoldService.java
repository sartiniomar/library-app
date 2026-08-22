package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.List;

public class PlacingOnHoldService {

  public DomainResult<Loan> placeOnHold(Patron patron, BookInstance book) {

    book.ensureCanBePlacedOnHoldBy(patron);

    Loan hold = new Loan(
        patron.getId(),
        book.getId()
    );

    BookPlacedOnHoldEvent event =
        new BookPlacedOnHoldEvent(patron.getId(), book.getId());

    return new DomainResult<>(hold, List.of(event));
  }
}
