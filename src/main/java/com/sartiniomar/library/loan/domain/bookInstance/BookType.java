package com.sartiniomar.library.loan.domain.bookInstance;

public enum BookType {
  CIRCULATING,
  RESTRICTED;

  public static class BookAlreadyOnHoldException extends RuntimeException {
    public BookAlreadyOnHoldException(String message) {
      super(message);
    }
  }
}
