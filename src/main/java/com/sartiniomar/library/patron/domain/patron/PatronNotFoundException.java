package com.sartiniomar.library.patron.domain.patron;

public class PatronNotFoundException extends RuntimeException {
  public PatronNotFoundException(String message) {
    super(message);
  }
}
