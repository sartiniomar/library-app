package com.sartiniomar.library.holding.model.patron;

public class OnlyResearcherCanHoldRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanHoldRestrictedBooksException(String message) {
    super(message);
  }
}
