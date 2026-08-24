package com.sartiniomar.library.loan.domain.bookInstance;

public class BookInstanceNotAvailableException extends RuntimeException {
  public BookInstanceNotAvailableException(String message) {
    super(message);
  }
}
