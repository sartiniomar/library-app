package com.sartiniomar.library.loan.domain.bookInstance;

public class OnlyResearcherCanRetiredRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanRetiredRestrictedBooksException(String message) {
    super(message);
  }
}
