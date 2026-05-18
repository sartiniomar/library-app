package com.sartiniomar.library.lending.domain.book;

import com.sartiniomar.library.lending.domain.hold.BookAlreadyOnHoldException;
import com.sartiniomar.library.lending.domain.patron.OnlyResearcherCanHoldRestrictedBooksException;
import com.sartiniomar.library.lending.domain.patron.Patron;
import java.util.UUID;

public class BookInstance {

  private final UUID id;
  private final String bookId;
  private final BookType type;
  private boolean onHold;

  protected BookInstance(String bookId, BookType type, boolean onHold) {
    this.id = UUID.randomUUID();
    this.bookId = bookId;
    this.type = type;
    this.onHold = onHold;
  }

  protected BookInstance(UUID id, String bookId, BookType type, boolean onHold) {
    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.onHold = onHold;
  }

  public static BookInstance circulating(String bookId) {
    return new BookInstance(bookId, BookType.CIRCULATING, false);
  }

  public static BookInstance restricted(String bookId) {
    return new BookInstance(bookId, BookType.RESTRICTED, false);
  }

  public static BookInstance restore(UUID id, String bookId, BookType type, boolean onHold) {
    return new BookInstance(id, bookId, type, onHold);
  }

  public UUID getId() {
    return this.id;
  }

  public String getBookId() {
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

  public void markOnHold() {
    if (onHold) {
      throw new BookAlreadyOnHoldException("Book Already On Hold");
    }
    this.onHold = true;
  }

  public void ensureCanBePlacedOnHoldBy(Patron patron) {

    if (isRestricted() && !patron.isResearcher()) {
      throw new OnlyResearcherCanHoldRestrictedBooksException(
          "Only Researcher Can Hold Restricted Books!"
      );
    }

    if (onHold) {
      throw new BookAlreadyOnHoldException("Book Already On Hold!");
    }
  }
}