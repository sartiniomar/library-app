package com.sartiniomar.library.lending.model.patron;

public class PatronNotFoundException extends RuntimeException {
  public PatronNotFoundException(String patronId) {
    super("Patron not found: " + patronId);
  }
}
