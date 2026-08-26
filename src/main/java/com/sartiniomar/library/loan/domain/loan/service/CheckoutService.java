package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanBookEvent;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.List;

public class CheckoutService {

  public DomainResult<Loan> checkout(Patron patron, BookInstance bookInstance) {
    DomainPolicy.ensurePatronCanLoanBook(patron, bookInstance);
    bookInstance.ensureCanBeCheckout();
    Loan hold = Loan.createLent(patron.getId(), bookInstance.getId());
    bookInstance.lent();
    LoanBookEvent event = new LoanBookEvent(patron.getId(), bookInstance.getId(), hold.getStatus().toString());
    return new DomainResult<>(hold, List.of(event));
  }

}
