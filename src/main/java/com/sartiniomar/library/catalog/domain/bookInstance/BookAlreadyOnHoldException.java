package com.sartiniomar.library.catalog.domain.bookInstance;

public class BookAlreadyOnHoldException extends RuntimeException {
  public BookAlreadyOnHoldException(String message) {
    super(message);
  }
}
