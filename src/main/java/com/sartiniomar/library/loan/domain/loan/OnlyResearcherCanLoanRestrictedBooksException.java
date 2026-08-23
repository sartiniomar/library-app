package com.sartiniomar.library.loan.domain.loan;

public class OnlyResearcherCanLoanRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanLoanRestrictedBooksException(String message) {
    super(message);
  }
}
