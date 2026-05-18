package com.sartiniomar.library.lending.domain.hold;

public class BookAlreadyOnHoldException extends RuntimeException {
  public BookAlreadyOnHoldException(String message) {
    super(message);
  }
}
