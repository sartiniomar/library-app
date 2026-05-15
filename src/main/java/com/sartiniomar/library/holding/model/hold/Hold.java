package com.sartiniomar.library.holding.model.hold;

import java.util.UUID;

public class Hold {

  private final UUID id;
  private final UUID patronId;
  private final UUID bookInstanceId;

  public Hold(UUID patronId, UUID bookInstanceId) {
    this.id = UUID.randomUUID();
    this.patronId = patronId;
    this.bookInstanceId = bookInstanceId;
  }

  public UUID getId() {
    return this.id;
  }

  public UUID getPatronId() {
    return this.patronId;
  }

  public UUID getBookInstanceId() {
    return bookInstanceId;
  }
}
