package com.sartiniomar.library.loan.domain.loan;

public class LoanLimitExceededException extends RuntimeException {
  public LoanLimitExceededException(String message) {
    super(message);
  }
}
