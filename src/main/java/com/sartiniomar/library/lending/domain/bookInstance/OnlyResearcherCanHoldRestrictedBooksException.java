package com.sartiniomar.library.lending.domain.bookInstance;

public class OnlyResearcherCanHoldRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanHoldRestrictedBooksException(String message) {
    super(message);
  }
}
