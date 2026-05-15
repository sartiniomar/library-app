package com.sartiniomar.library.lending.application.port.in;

import java.util.UUID;

public class PlaceHoldCommand {

  private final UUID patronId;
  private final UUID bookInstanceId;

  public PlaceHoldCommand(UUID patronId, UUID bookInstanceId) {
    this.patronId = patronId;
    this.bookInstanceId = bookInstanceId;
  }

  public UUID getPatronId() {
    return patronId;
  }

  public UUID getBookId() {
    return bookInstanceId;
  }
}
