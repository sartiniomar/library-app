package com.sartiniomar.library.loan.support.builder;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import java.util.UUID;

public class BookInstanceLoanTestDataBuilder {
  public BookInstance buildCirculatingDefault(UUID bookId) {return BookInstance.circulating(bookId);}

  public BookInstance build(UUID bookId, BookType type, BookInstanceStatus status) {
    return new BookInstance(UUID.randomUUID(), bookId, type, status);
  }
}
