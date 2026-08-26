package com.sartiniomar.library.catalog.domain.bookInstance;

import com.sartiniomar.library.loan.domain.loan.TransitionStatusException;
import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private final UUID bookId;
  private BookType type;
  private BookInstanceStatus status;

  public BookInstance(UUID id, UUID bookId, BookType type, BookInstanceStatus status) {
    if (bookId == null) {
      throw new IllegalArgumentException("Book ID cannot be empty");
    }
    if (type == null) {
      throw new IllegalArgumentException("Book type cannot be empty");
    }
    if (status == null) {
      throw new IllegalArgumentException("Status cannot by empty");
    }

    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.status = status;
  }

  public static BookInstance circulating(UUID bookId) {
    return new BookInstance(UUID.randomUUID(), bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
  }

  public static BookInstance restricted(UUID bookId) {
    return new BookInstance(UUID.randomUUID(), bookId, BookType.RESTRICTED, BookInstanceStatus.AVAILABLE);
  }

  public void update(BookType type, BookInstanceStatus status) {
    if (type != null) this.type = type;
    if (status != null) this.status = status;
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

  public void setType(BookType type) {
    this.type = type;
  }

  public void setStatus(BookInstanceStatus status) {
    this.status = status;
  }

  public void unavailable() {
    if (status == BookInstanceStatus.UNAVAILABLE) {
      throw new TransitionStatusException(
          "You cannot change from status " + status + " to status " + BookInstanceStatus.UNAVAILABLE);
    }
    this.status = BookInstanceStatus.UNAVAILABLE;
  }
}