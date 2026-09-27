package com.sartiniomar.library.loan.domain.loan.exception;

public class OperationNotPermittedException extends RuntimeException {
  public OperationNotPermittedException(String message) {
    super(message);
  }
}
