package com.sartiniomar.library.loan.domain.bookInstance;

import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private final UUID bookId;
  private final BookType type;
  private final boolean onLoan;

  public BookInstance(UUID id, UUID bookId, BookType type, boolean onLoan) {
    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.onLoan = onLoan;
  }

  public UUID getId() {
    return this.id;
  }

  public UUID getBookId() {
    return bookId;
  }

  public BookType getType() {
    return type;
  }

  public boolean isRestricted() {
    return type == BookType.RESTRICTED;
  }

  public boolean isOnLoan() {
    return onLoan;
  }

  public void ensureCanBeReserved() {
    if (onLoan) {
      throw new BookAlreadyOnLoanException("Book Already On Hold!");
    }
  }
}