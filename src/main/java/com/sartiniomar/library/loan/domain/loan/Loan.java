package com.sartiniomar.library.loan.domain.loan;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Loan {

  private final UUID id;
  private final UUID patronId;
  private final UUID bookInstanceId;
  private LoanStatus status;
  private final Instant reservedAt;
  private Instant lentAt;
  private Instant dueAt;
  private Instant returnedAt;

  private static final Integer RESERVED_LIMIT_DAYS = 3;
  public static final List<LoanStatus> ACTIVE_STATUSES =
      List.of(LoanStatus.RESERVED, LoanStatus.LENT, LoanStatus.DELAYED);

  private Loan(
      UUID id,
      UUID patronId,
      UUID bookInstanceId,
      LoanStatus status,
      Instant reservedAt,
      Instant lentAt,
      Instant dueAt,
      Instant returnedAt
  ) {
    this.id = id;
    this.patronId = patronId;
    this.bookInstanceId = bookInstanceId;
    this.status = status;
    this.reservedAt = reservedAt;
    this.lentAt = lentAt;
    this.dueAt = dueAt;
    this.returnedAt = returnedAt;
  }

  public static Loan createReserve(UUID patronId, UUID bookInstanceId, Clock clock) {
    Instant now = Instant.now(clock);
    return new Loan(
        UUID.randomUUID(),
        patronId,
        bookInstanceId,
        LoanStatus.RESERVED,
        now,
        null,
        now.plus(Duration.ofDays(RESERVED_LIMIT_DAYS)),
        null
    );
  }

  public static Loan createLent(UUID patronId, UUID bookInstanceId, Clock clock, Integer days) {
    Instant now = Instant.now(clock);
    return new Loan(
        UUID.randomUUID(),
        patronId,
        bookInstanceId,
        LoanStatus.LENT,
        null,
        now,
        now.plus(Duration.ofDays(days)),
        null
    );
  }

  public static Loan withId(
      UUID id,
      UUID patronId,
      UUID bookInstanceId,
      LoanStatus status,
      Instant reservedAt,
      Instant lentAt,
      Instant dueAt,
      Instant returnedAt
  ) {
    return new Loan(id, patronId, bookInstanceId, status, reservedAt, lentAt, dueAt, returnedAt);
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

  public void cancelled() {
    if (this.status != LoanStatus.RESERVED) {
      throw new TransitionStatusException(
          "You cannot change from status " + this.status + " to status " + LoanStatus.CANCELLED);
    }
    this.status = LoanStatus.CANCELLED;
  }

  public void lent(Integer days, Clock clock) {
    if (this.status != LoanStatus.RESERVED) {
      throw new TransitionStatusException(
          "You cannot change from status " + this.status.toString() + " to status " + LoanStatus.LENT);
    }
    Instant now = Instant.now(clock);
    this.status = LoanStatus.LENT;
    this.lentAt = now;
    this.dueAt = now.plus(Duration.ofDays(days));
  }

  public void returned() {
    if (this.status == LoanStatus.LENT) {
      this.status = LoanStatus.RETURNED;
    } else if (this.status == LoanStatus.DELAYED) {
      this.status = LoanStatus.RETURNED_WITH_DELAY;
    } else {
      throw new TransitionStatusException("You cannot change status");
    }
    this.returnedAt = Instant.now();
  }

  public void delayed() {
    if (this.status != LoanStatus.LENT) {
      throw new TransitionStatusException(
          "You cannot change from status " + this.status + " to status " + LoanStatus.DELAYED);
    }
    this.status = LoanStatus.DELAYED;
  }

  public void ensureCanBeCancelled() {
    if (this.status != LoanStatus.RESERVED) {
      throw new OperationNotPermittedException("The loan is not reserved for cancelled!");
    }
  }

  public void ensureCanBeReturned() {
    if (this.status != LoanStatus.LENT && this.status != LoanStatus.DELAYED) {
      throw new OperationNotPermittedException("The loan is not lent or delayed for returned!");
    }
  }
}