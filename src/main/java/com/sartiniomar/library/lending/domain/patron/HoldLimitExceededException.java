package com.sartiniomar.library.lending.domain.patron;

public class HoldLimitExceededException extends RuntimeException {
  public HoldLimitExceededException(String message) {
    super(message);
  }
}
