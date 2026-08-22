package com.sartiniomar.library.catalog.support.builder;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import java.util.UUID;

public class BookInstanceTestDataBuilder {

  private final UUID bookId = UUID.fromString("c2fd7af9-041b-477e-baac-431cbc048e07");

  public BookInstance buildCirculatingDefault() {return BookInstance.circulating(bookId);}

  public BookInstance buildRestrictedDefault() {return BookInstance.restricted(bookId);}

  public BookInstance build(UUID bookId, BookType type, Boolean onHold) {
    return new BookInstance(UUID.randomUUID(), bookId, type, onHold);
  }
}
