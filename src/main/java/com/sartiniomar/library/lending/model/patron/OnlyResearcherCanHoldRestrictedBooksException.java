package com.sartiniomar.library.lending.model.patron;

public class OnlyResearcherCanHoldRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanHoldRestrictedBooksException(String message) {
    super(message);
  }
}
