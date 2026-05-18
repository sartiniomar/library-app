package com.sartiniomar.library.catalog.domain.book;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(String bookId) {
    super("Book not found: " + bookId);
  }
}
