package com.sartiniomar.library.patron.domain.patron;

public class PatronAlreadyExistsException extends RuntimeException {
  public PatronAlreadyExistsException(String message) {
    super(message);
  }
}
