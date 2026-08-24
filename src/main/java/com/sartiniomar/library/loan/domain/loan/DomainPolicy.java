package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.patron.Patron;

public class DomainPolicy {

  public static void ensurePatronCanLoanBook(Patron patron, BookInstance bookInstance) {
    if (bookInstance.isRestricted() && !patron.isResearcher()) {
      throw new OnlyResearcherCanLoanRestrictedBooksException("Only Researcher Can Loan Restricted Books!");
    }
  }
}
