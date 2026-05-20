package com.sartiniomar.library.patron.domain.patron;

public class PatronNotFoundException extends RuntimeException {
  public PatronNotFoundException(String patronId) {
    super("Patron not found: " + patronId);
  }
}
