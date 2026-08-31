package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.time.Clock;

public class CheckoutServiceDomain {

  private final Clock clock;

  public CheckoutServiceDomain(Clock clock) {
    this.clock = clock;
  }

  public Loan checkout(Patron patron, BookInstance bookInstance) {
    DomainPolicy.ensurePatronCanLoanBook(patron, bookInstance);
    bookInstance.ensureCanBeCheckout();
    Loan loan = Loan.createLent(patron.getId(), bookInstance.getId(), clock, patron.getLimitDays());
    bookInstance.lent();
    return loan;
  }
}
