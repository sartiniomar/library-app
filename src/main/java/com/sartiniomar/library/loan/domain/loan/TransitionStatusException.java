package com.sartiniomar.library.loan.domain.loan;

public class TransitionStatusException extends RuntimeException {
  public TransitionStatusException(String message) {
    super(message);
  }
}
