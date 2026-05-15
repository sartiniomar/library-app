package com.sartiniomar.library.lending.model.patron;

public class HoldLimitExceededException extends RuntimeException {
  public HoldLimitExceededException(String message) {
    super(message);
  }
}
