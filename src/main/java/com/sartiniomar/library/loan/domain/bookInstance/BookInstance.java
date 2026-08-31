package com.sartiniomar.library.loan.domain.bookInstance;

import com.sartiniomar.library.loan.domain.loan.TransitionStatusException;
import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private final UUID bookId;
  private final BookType type;
  private BookInstanceStatus status;

  private final String BOOK_ALREADY_UNAVAILABLE_MESSAGE = "Book Already Unavailable!";

  public BookInstance(UUID id, UUID bookId, BookType type, BookInstanceStatus status) {
    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.status = status;
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

  public void reserved() {
    if (this.status != BookInstanceStatus.AVAILABLE) {
      throw new TransitionStatusException(
          "You cannot change from status " + this.status + " to status " + BookInstanceStatus.RESERVED);
    }
    this.status = BookInstanceStatus.RESERVED;
  }

  public void lent() {
    if (this.status != BookInstanceStatus.AVAILABLE && this.status != BookInstanceStatus.RESERVED) {
      throw new TransitionStatusException(
          "You cannot change from status " + this.status + " to status " + BookInstanceStatus.LENT);
    }
    this.status = BookInstanceStatus.LENT;
  }

  public void available() {
    if (this.status == BookInstanceStatus.AVAILABLE) {
      throw new TransitionStatusException(
          "You cannot change from status " + this.status + " to status " + BookInstanceStatus.AVAILABLE);
    }
    this.status = BookInstanceStatus.AVAILABLE;
  }

  public void ensureCanBeReserved() {
    if (this.status != BookInstanceStatus.AVAILABLE) {
      throw new BookInstanceNotAvailableException(BOOK_ALREADY_UNAVAILABLE_MESSAGE);
    }
  }

  public void ensureCanBeCheckout() {
    if (this.status != BookInstanceStatus.AVAILABLE) {
      throw new BookInstanceNotAvailableException(BOOK_ALREADY_UNAVAILABLE_MESSAGE);
    }
  }
}