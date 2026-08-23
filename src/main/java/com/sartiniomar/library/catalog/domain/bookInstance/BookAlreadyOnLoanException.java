package com.sartiniomar.library.catalog.domain.bookInstance;

public class BookAlreadyOnLoanException extends RuntimeException {
  public BookAlreadyOnLoanException(String message) {
    super(message);
  }
}
