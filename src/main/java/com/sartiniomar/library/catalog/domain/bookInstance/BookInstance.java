package com.sartiniomar.library.catalog.domain.bookInstance;

import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private UUID bookId;
  private BookType type;
  private BookInstanceStatus status;
  private Boolean onLoan;

  public BookInstance(UUID id, UUID bookId, BookType type, BookInstanceStatus status, Boolean onLoan) {
    if (bookId == null) {
      throw new IllegalArgumentException("Book ID cannot be empty");
    }
    if (type == null) {
      throw new IllegalArgumentException("Book type cannot be empty");
    }
    if (status == null) {
      throw new IllegalArgumentException("Status cannot by empty");
    }
    if (onLoan == null) {
      throw new IllegalArgumentException("On loan status cannot be empty");
    }

    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.status = status;
    this.onLoan = onLoan;
  }

  public static BookInstance circulating(UUID bookId) {
    return new BookInstance(UUID.randomUUID(), bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE, false);
  }

  public static BookInstance restricted(UUID bookId) {
    return new BookInstance(UUID.randomUUID(), bookId, BookType.RESTRICTED, BookInstanceStatus.AVAILABLE, false);
  }

  public void update(UUID bookId, BookType type, BookInstanceStatus status, Boolean onLoan) {
    if (bookId != null) this.bookId = bookId;
    if (type != null) this.type = type;
    if (status != null) this.status = status;
    if (onLoan != null) this.onLoan = onLoan;
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

  public BookInstanceStatus getStatus() {
    return status;
  }

  public boolean isRestricted() {
    return type == BookType.RESTRICTED;
  }

  public boolean isOnLoan() {
    return onLoan;
  }

  public void setBookId(UUID bookId) {
    this.bookId = bookId;
  }

  public void setType(BookType type) {
    this.type = type;
  }

  public void setStatus(BookInstanceStatus status) {
    this.status = status;
  }

  public void setOnLoan(boolean onLoan) {
    this.onLoan = onLoan;
  }

  public void markOnLoan() {
    if (this.onLoan) {
      throw new BookAlreadyOnLoanException("Book is already on loan");
    }
    this.onLoan = true;
  }
}