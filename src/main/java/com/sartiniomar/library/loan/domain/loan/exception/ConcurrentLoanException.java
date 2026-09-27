package com.sartiniomar.library.loan.domain.loan.exception;

public class ConcurrentLoanException extends RuntimeException {
  public ConcurrentLoanException(String message) {
    super(message);
  }
}
