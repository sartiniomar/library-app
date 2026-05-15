package com.sartiniomar.library.holding.model.hold;

public class BookAlreadyOnHoldException extends RuntimeException {
  public BookAlreadyOnHoldException(String message) {
    super(message);
  }
}
