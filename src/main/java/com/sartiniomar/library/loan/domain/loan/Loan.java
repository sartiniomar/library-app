package com.sartiniomar.library.loan.domain.loan;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotAvailableException;
import java.time.Instant;
import java.util.UUID;

public class Loan {

  private final UUID id;
  private final UUID patronId;
  private final UUID bookInstanceId;
  private LoanStatus status;
  private final Instant reservedAt;
  private Instant lentAt;
  private final Instant dueAt;
  private Instant returnedAt;

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

  public void cancelled() {
    if (status != LoanStatus.RESERVED) {
      throw new TransitionStatusException(
          "You cannot change from status " + status.toString() + " to status " + LoanStatus.CANCELLED);
    }
    this.status = LoanStatus.CANCELLED;
  }

  public void lent() {
    if (status != LoanStatus.RESERVED) {
      throw new TransitionStatusException(
          "You cannot change from status " + status.toString() + " to status " + LoanStatus.LENT);
    }
    this.status = LoanStatus.LENT;
    this.lentAt = Instant.now();
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
    if (status != LoanStatus.LENT) {
      throw new TransitionStatusException(
          "You cannot change from status " + status.toString() + " to status " + LoanStatus.DELAYED);
    }
    this.status = LoanStatus.DELAYED;
  }

  public void ensureCanBeCancelled() {
    if (this.status != LoanStatus.RESERVED) {
      throw new BookInstanceNotAvailableException("The loan is not reserved!");
    }
  }

  public void ensureCanBeReturned() {
    if (this.status != LoanStatus.LENT && this.status != LoanStatus.DELAYED) {
      throw new BookInstanceNotAvailableException("The loan is not lent or delayed!");
    }
  }
}
