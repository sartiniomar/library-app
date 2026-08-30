package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.patron.Patron;

public class ReturnServiceDomain {

  public Loan returned(Loan loan, Patron patron, BookInstance bookInstance) {
    loan.ensureCanBeReturned();
    loan.returned();
    bookInstance.available();
    return loan;
  }
}
