package com.sartiniomar.library.loan.domain.loan.exception;

public class TransitionStatusException extends RuntimeException {
  public TransitionStatusException(String message) {
    super(message);
  }
}
