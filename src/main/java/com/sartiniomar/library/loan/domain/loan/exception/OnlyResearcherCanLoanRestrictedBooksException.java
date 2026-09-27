package com.sartiniomar.library.loan.domain.loan.exception;

public class OnlyResearcherCanLoanRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanLoanRestrictedBooksException(String message) {
    super(message);
  }
}
