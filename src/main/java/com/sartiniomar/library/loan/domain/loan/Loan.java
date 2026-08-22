package com.sartiniomar.library.loan.domain.loan;

import java.util.UUID;

public class Loan {

  private final UUID id;
  private final UUID patronId;
  private final UUID bookInstanceId;

  public Loan(UUID patronId, UUID bookInstanceId) {
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
