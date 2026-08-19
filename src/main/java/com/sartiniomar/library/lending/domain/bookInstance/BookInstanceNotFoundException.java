package com.sartiniomar.library.lending.domain.bookInstance;

public class BookInstanceNotFoundException extends RuntimeException {
  public BookInstanceNotFoundException(String bookId) {
    super("Book Instance not found: " + bookId);
  }
}
