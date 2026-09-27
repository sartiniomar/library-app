package com.sartiniomar.library.loan.domain.loan.exception;

public class LoanNotFoundException extends RuntimeException {
  public LoanNotFoundException(String message) {super("Loan not found: " + message);
  }
}
