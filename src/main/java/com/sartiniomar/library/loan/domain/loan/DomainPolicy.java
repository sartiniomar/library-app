package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.patron.Patron;

public class DomainPolicy {

  public static final Integer MAX_VALUE_LOANS_REGULAR_PATRON = 3;

  public static void ensurePatronCanLoanBook(Patron patron, BookInstance bookInstance) {
    if (bookInstance.isRestricted() && !patron.isResearcher()) {
      throw new OnlyResearcherCanLoanRestrictedBooksException("Only Researcher Can Loan Restricted Books!");
    }
  }

  public static void ensureCanHaveAnotherLoan(Patron patron, long activeLoans) {
    if (patron.isRegular() && activeLoans >= MAX_VALUE_LOANS_REGULAR_PATRON) {
      throw new LoanLimitExceededException("Loan Limit Exceeded.");
    }
  }
}
