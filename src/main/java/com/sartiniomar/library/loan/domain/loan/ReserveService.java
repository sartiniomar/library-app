package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.List;

public class ReserveService {

  public DomainResult<Loan> reserve(Patron patron, BookInstance book) {
    ensureCanBeReserved(patron, book);
    Loan hold = Loan.createReserve(patron.getId(), book.getId());
    ReserveBookEvent event = new ReserveBookEvent(patron.getId(), book.getId());
    return new DomainResult<>(hold, List.of(event));
  }

  private void ensureCanBeReserved(Patron patron, BookInstance book) {
    if (book.isRestricted() && !patron.isResearcher()) {
      throw new OnlyResearcherCanLoanRestrictedBooksException("Only Researcher Can Hold Restricted Books!");
    }
    book.ensureCanBeReserved();
  }
}
