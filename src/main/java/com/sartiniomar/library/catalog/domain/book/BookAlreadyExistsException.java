package com.sartiniomar.library.catalog.domain.book;

public class BookAlreadyExistsException extends RuntimeException {
  public BookAlreadyExistsException(String message) {
    super(message);
  }
}
