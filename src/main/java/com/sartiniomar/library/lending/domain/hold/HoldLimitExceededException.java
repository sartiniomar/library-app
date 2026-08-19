package com.sartiniomar.library.lending.domain.hold;

public class HoldLimitExceededException extends RuntimeException {
  public HoldLimitExceededException(String message) {
    super(message);
  }
}
