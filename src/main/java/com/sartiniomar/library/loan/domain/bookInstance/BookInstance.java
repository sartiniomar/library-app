package com.sartiniomar.library.loan.domain.bookInstance;

import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private final UUID bookId;
  private final BookType type;
  private final boolean onHold;

  protected BookInstance(UUID bookId, BookType type, boolean onHold) {
    this.id = UUID.randomUUID();
    this.bookId = bookId;
    this.type = type;
    this.onHold = onHold;
  }

  public BookInstance(UUID id, UUID bookId, BookType type, boolean onHold) {
    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.onHold = onHold;
  }

  public static BookInstance circulating(UUID bookId) {
    return new BookInstance(bookId, BookType.CIRCULATING, false);
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

  public void ensureCanBePlacedOnHoldBy(Patron patron) {

    if (isRestricted() && !patron.isResearcher()) {
      throw new OnlyResearcherCanRetiredRestrictedBooksException(
          "Only Researcher Can Hold Restricted Books!"
      );
    }

    if (onHold) {
      throw new BookType.BookAlreadyOnHoldException("Book Already On Hold!");
    }
  }
}