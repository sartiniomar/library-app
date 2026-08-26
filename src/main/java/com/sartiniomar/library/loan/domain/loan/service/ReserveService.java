package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanBookEvent;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.List;

public class ReserveService {

  public DomainResult<Loan> reserve(Patron patron, BookInstance bookInstance) {
    DomainPolicy.ensurePatronCanLoanBook(patron, bookInstance);
    bookInstance.ensureCanBeReserved();
    Loan reserve = Loan.createReserve(patron.getId(), bookInstance.getId());
    bookInstance.reserved();
    LoanBookEvent event = new LoanBookEvent(patron.getId(), bookInstance.getId(), reserve.getStatus().toString());
    return new DomainResult<>(reserve, List.of(event));
  }
}
