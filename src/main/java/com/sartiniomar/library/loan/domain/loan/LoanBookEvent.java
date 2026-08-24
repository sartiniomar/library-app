package com.sartiniomar.library.loan.domain.loan;

import java.util.UUID;

public class LoanBookEvent {

  private final UUID patronId;
  private final UUID bookId;
  private final String status;

  public LoanBookEvent(UUID patronId, UUID bookId, String status) {
    this.patronId = patronId;
    this.bookId = bookId;
    this.status = status;
  }

  public UUID getPatronId() {
    return patronId;
  }

  public UUID getBookId() {
    return bookId;
  }

  public String getStatus() {
    return status;
  }
}
