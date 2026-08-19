package com.sartiniomar.library.patron.application.port.in;

public record CreatePatronCommand (
    String name,
    String email
) {
  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
}
