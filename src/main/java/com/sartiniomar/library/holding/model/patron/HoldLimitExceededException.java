package com.sartiniomar.library.holding.model.patron;

public class HoldLimitExceededException extends RuntimeException {
  public HoldLimitExceededException(String message) {
    super(message);
  }
}
