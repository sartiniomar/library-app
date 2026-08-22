package com.sartiniomar.library.catalog.domain.bookInstance;

public class OnlyResearcherCanHoldRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanHoldRestrictedBooksException(String message) {
    super(message);
  }
}
