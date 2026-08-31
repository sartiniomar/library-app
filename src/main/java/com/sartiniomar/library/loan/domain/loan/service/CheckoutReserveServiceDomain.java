package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.time.Clock;

public class CheckoutReserveServiceDomain {

  private final Clock clock;

  public CheckoutReserveServiceDomain(Clock clock) {
    this.clock = clock;
  }

  public Loan checkoutReserve(Loan loan, Patron patron, BookInstance bookInstance) {
    loan.lent(patron.getLimitDays(), clock);
    bookInstance.lent();
    return loan;
  }
}
