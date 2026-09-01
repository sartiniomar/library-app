package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.Loan;

public class ReturnServiceDomain {

  public Loan returned(Loan loan, BookInstance bookInstance) {
    loan.ensureCanBeReturned();
    loan.returned();
    bookInstance.available();
    return loan;
  }
}
