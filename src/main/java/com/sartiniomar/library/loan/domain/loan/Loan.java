package com.sartiniomar.library.loan.domain.loan;

import java.time.Instant;
import java.util.UUID;

public class Loan {

  private final UUID id;
  private final UUID patronId;
  private final UUID bookInstanceId;
  private final LoanStatus status;
  private final Instant reservedAt;
  private final Instant lentAt;
  private final Instant dueAt;
  private final Instant returnedAt;

  public Loan(UUID patronId, UUID bookInstanceId, LoanStatus status, Instant reservedAt, Instant lentAt, Instant dueAt, Instant returnedAt) {
    this.id = UUID.randomUUID();
    this.patronId = patronId;
    this.bookInstanceId = bookInstanceId;
    this.status = status;
    this.reservedAt = reservedAt;
    this.lentAt = lentAt;
    this.dueAt = dueAt;
    this.returnedAt = returnedAt;
  }

  public static Loan createReserve(UUID patronId, UUID bookInstanceId) {
    return new Loan(patronId, bookInstanceId, LoanStatus.RESERVED, Instant.now(), null, null, null);
  }

  public static Loan createLent(UUID patronId, UUID bookInstanceId) {
    return new Loan(patronId, bookInstanceId, LoanStatus.LENT, null, Instant.now(), null, null);
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

  public LoanStatus getStatus() {
    return status;
  }

  public Instant getReservedAt() {
    return reservedAt;
  }

  public Instant getLentAt() {
    return lentAt;
  }

  public Instant getDueAt() {
    return dueAt;
  }

  public Instant getReturnedAt() {
    return returnedAt;
  }
}
