package com.sartiniomar.library.holding.model.patron;

public class PatronNotFoundException extends RuntimeException {
  public PatronNotFoundException(String patronId) {
    super("Patron not found: " + patronId);
  }
}
