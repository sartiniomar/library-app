package com.sartiniomar.library.lending.model.book;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(UUID bookId) {
    super("Book not found: " + bookId);
  }
}
