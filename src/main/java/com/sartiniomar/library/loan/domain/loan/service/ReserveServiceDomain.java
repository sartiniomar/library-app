package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.time.Clock;

public class ReserveServiceDomain {

  private final Clock clock;

  public ReserveServiceDomain(Clock clock) {
    this.clock = clock;
  }

  public Loan reserve(Patron patron, BookInstance bookInstance) {
    DomainPolicy.ensurePatronCanLoanBook(patron, bookInstance);
    bookInstance.ensureCanBeReserved();
    Loan reserve = Loan.createReserve(patron.getId(), bookInstance.getId(), clock);
    bookInstance.reserved();
    return reserve;
  }
}
