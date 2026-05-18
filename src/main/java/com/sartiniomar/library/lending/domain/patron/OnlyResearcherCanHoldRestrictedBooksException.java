package com.sartiniomar.library.lending.domain.patron;

public class OnlyResearcherCanHoldRestrictedBooksException extends RuntimeException {
  public OnlyResearcherCanHoldRestrictedBooksException(String message) {
    super(message);
  }
}
