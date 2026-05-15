package com.sartiniomar.library.lending.model.hold;

public class BookAlreadyOnHoldException extends RuntimeException {
  public BookAlreadyOnHoldException(String message) {
    super(message);
  }
}
