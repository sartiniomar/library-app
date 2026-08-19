package com.sartiniomar.library.lending.domain.bookInstance;

public enum BookType {
  CIRCULATING,
  RESTRICTED;

  public static class BookAlreadyOnHoldException extends RuntimeException {
    public BookAlreadyOnHoldException(String message) {
      super(message);
    }
  }
}
