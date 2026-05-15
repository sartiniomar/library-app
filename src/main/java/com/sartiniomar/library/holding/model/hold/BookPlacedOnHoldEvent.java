package com.sartiniomar.library.holding.model.hold;

import java.util.UUID;

public class BookPlacedOnHoldEvent {

  private final UUID patronId;
  private final UUID bookId;

  public BookPlacedOnHoldEvent(UUID patronId, UUID bookId) {
    this.patronId = patronId;
    this.bookId = bookId;
  }

  public UUID getPatronId() {
    return patronId;
  }

  public UUID getBookId() {
    return bookId;
  }
}
