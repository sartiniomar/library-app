package com.sartiniomar.library.loan.domain.loan.reserve;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.List;

public class ReserveService {

  public DomainResult<Loan> reserve(Patron patron, BookInstance bookInstance) {
    DomainPolicy.ensurePatronCanLoanBook(patron, bookInstance);
    bookInstance.ensureCanBeReserved();
    Loan hold = Loan.createReserve(patron.getId(), bookInstance.getId());
    ReserveBookEvent event = new ReserveBookEvent(patron.getId(), bookInstance.getId());
    return new DomainResult<>(hold, List.of(event));
  }
}
