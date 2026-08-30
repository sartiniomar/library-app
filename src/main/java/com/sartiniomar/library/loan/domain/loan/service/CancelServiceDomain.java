package com.sartiniomar.library.loan.domain.loan.service;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.Loan;

public class CancelServiceDomain {

  public Loan cancel(Loan loan, BookInstance bookInstance) {
    loan.ensureCanBeCancelled();
    loan.cancelled();
    bookInstance.available();
    return loan;
  }
}
