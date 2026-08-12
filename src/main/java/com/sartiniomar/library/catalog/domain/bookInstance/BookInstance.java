package com.sartiniomar.library.catalog.domain.bookInstance;

import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private UUID bookId;
  private BookType type;
  private Boolean onHold;

  public BookInstance(UUID id, UUID bookId, BookType type, Boolean onHold) {
    if (bookId == null) {
      throw new IllegalArgumentException("Book ID cannot be empty");
    }
    if (type == null) {
      throw new IllegalArgumentException("Book type cannot be empty");
    }
    if (onHold == null) {
      throw new IllegalArgumentException("On hold status cannot be empty");
    }

    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.onHold = onHold;
  }

  public static BookInstance circulating(UUID bookId) {
    return new BookInstance(UUID.randomUUID(), bookId, BookType.CIRCULATING, false);
  }

  public static BookInstance restricted(UUID bookId) {
    return new BookInstance(UUID.randomUUID(), bookId, BookType.RESTRICTED, false);
  }

  public void update(UUID bookId, BookType type, Boolean onHold) {
    if (bookId != null) this.bookId = bookId;
    if (type != null) this.type = type;
    if (onHold != null) this.onHold = onHold;
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

  public boolean isOnHold() {
    return onHold;
  }

  public void setBookId(UUID bookId) {
    this.bookId = bookId;
  }

  public void setType(BookType type) {
    this.type = type;
  }

  public void setOnHold(boolean onHold) {
    this.onHold = onHold;
  }

  public void markOnHold() {
    if (this.onHold) {
      throw new BookAlreadyOnHoldException("Book is already on hold");
    }
    this.onHold = true;
  }
}