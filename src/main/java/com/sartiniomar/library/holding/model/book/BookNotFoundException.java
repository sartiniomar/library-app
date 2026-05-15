package com.sartiniomar.library.holding.model.book;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(UUID bookId) {
    super("Book not found: " + bookId);
  }
}
