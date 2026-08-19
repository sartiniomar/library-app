package com.sartiniomar.library.catalog.domain.bookInstance;

public class BookInstanceNotFoundException extends RuntimeException {
  public BookInstanceNotFoundException(String message) {
    super(message);
  }
}
