package com.sartiniomar.library.loan.domain.bookInstance;

public class BookAlreadyOnLoanException extends RuntimeException {
  public BookAlreadyOnLoanException(String message) {
    super(message);
  }
}
