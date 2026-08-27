package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanBookEvent;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.List;

public class CancelledService {

  public DomainResult<Loan> cancelled(Loan loan,Patron patron, BookInstance bookInstance) {
    loan.ensureCanBeCancelled();
    loan.cancelled();
    bookInstance.available();
    LoanBookEvent event = new LoanBookEvent(patron.getId(), bookInstance.getId(), loan.getStatus().toString());
    return new DomainResult<>(loan, List.of(event));
  }
}
