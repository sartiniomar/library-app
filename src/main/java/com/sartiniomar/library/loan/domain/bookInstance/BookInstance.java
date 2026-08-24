package com.sartiniomar.library.loan.domain.bookInstance;

import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private final UUID bookId;
  private final BookType type;
  private final BookInstanceStatus status;
  private final boolean onLoan;

  public BookInstance(UUID id, UUID bookId, BookType type, BookInstanceStatus status, boolean onLoan) {
    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.status = status;
    this.onLoan = onLoan;
  }

  public UUID getId() {
    return this.id;
  }

  public UUID getBookId() {
    return this.bookId;
  }

  public BookType getType() {
    return this.type;
  }

  public BookInstanceStatus getStatus() {
    return this.status;
  }

  public boolean isRestricted() {
    return this.type == BookType.RESTRICTED;
  }

  public boolean isOnLoan() {
    return this.onLoan;
  }

  public void ensureCanBeReserved() {
    if (this.status != BookInstanceStatus.AVAILABLE) {
      throw new BookInstanceNotAvailableException("Book Already Unavailable!");
    }
  }

  public void ensureCanBeCheckout() {
    if (this.status != BookInstanceStatus.AVAILABLE && this.status != BookInstanceStatus.RESERVED) {
      throw new BookInstanceNotAvailableException("Book Already Unavailable!");
    }
  }
}