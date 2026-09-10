package com.sartiniomar.library.loan.domain.loan;

public class OperationNotPermittedException extends RuntimeException {
  public OperationNotPermittedException(String message) {
    super(message);
  }
}
