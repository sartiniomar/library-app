package com.sartiniomar.library.catalog.domain.bookInstance;

public class OnlyResearcherCanLoanRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanLoanRestrictedBooksException(String message) {
    super(message);
  }
}
