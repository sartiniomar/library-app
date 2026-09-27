package com.sartiniomar.library.loan.domain.loan.exception;

public class LoanLimitExceededException extends RuntimeException {
  public LoanLimitExceededException(String message) {
    super(message);
  }
}
