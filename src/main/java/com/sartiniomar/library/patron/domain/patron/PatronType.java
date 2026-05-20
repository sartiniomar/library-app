package com.sartiniomar.library.patron.domain.patron;

public enum PatronType {
  REGULAR,
  RESEARCHER;

  public static PatronType fromString(String type) {
    return switch (type.toUpperCase()) {
      case "REGULAR" -> REGULAR;
      case "RESEARCHER" -> RESEARCHER;
      default -> throw new IllegalArgumentException("Unknown patron type: " + type);
    };
  }
}
