package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.List;

public class DomainPolicy {

  public static final List<LoanStatus> ACTIVE_STATUSES = List.of(LoanStatus.RESERVED, LoanStatus.LENT, LoanStatus.DELAYED);
  public static final Integer MAX_VALUE_LOANS_REGULAR_PATRON = 3;
  public static final Integer REGULAR_PATRON_LEND_LIMIT_DAYS = 7;
  public static final Integer RESEARCHER_PATRON_LEND_LIMIT_DAYS = 14;

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
