package com.sartiniomar.library.loan.domain.loan;

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
