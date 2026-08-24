package com.sartiniomar.library.loan.domain.loan.reserve;

import java.util.UUID;

public class ReserveBookEvent {

  private final UUID patronId;
  private final UUID bookId;

  public ReserveBookEvent(UUID patronId, UUID bookId) {
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
