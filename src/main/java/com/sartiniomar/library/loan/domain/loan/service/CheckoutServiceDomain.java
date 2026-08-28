package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.DomainResult;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanBookEvent;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import java.time.Clock;
import java.util.List;

public class CheckoutServiceDomain {

  private final Clock clock;

  public CheckoutServiceDomain(Clock clock) {
    this.clock = clock;
  }

  public DomainResult<Loan> checkout(Patron patron, BookInstance bookInstance) {
    DomainPolicy.ensurePatronCanLoanBook(patron, bookInstance);
    bookInstance.ensureCanBeCheckout();
    Loan loan = Loan.createLent(patron.getId(), bookInstance.getId(), clock, getLimitDays(patron.getType()));
    bookInstance.lent();
    LoanBookEvent event = new LoanBookEvent(patron.getId(), bookInstance.getId(), loan.getStatus().toString());
    return new DomainResult<>(loan, List.of(event));
  }

  private Integer getLimitDays(PatronType patronType) {
    return PatronType.REGULAR.equals(patronType) ?
        DomainPolicy.REGULAR_PATRON_LEND_LIMIT_DAYS : DomainPolicy.RESEARCHER_PATRON_LEND_LIMIT_DAYS;
  }
}
