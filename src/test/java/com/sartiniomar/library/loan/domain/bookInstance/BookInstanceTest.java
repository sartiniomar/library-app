package com.sartiniomar.library.loan.domain.bookInstance;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookInstanceTest {

  @Test
  void should_create_successfully_book_instance() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.CIRCULATING, false);

    assertEquals(id, book.getId());
    assertEquals(bookId, book.getBookId());
    assertEquals(BookType.CIRCULATING, book.getType());
    assertFalse(book.isOnLoan());
  }

  @Test
  void should_throw_exception_when_book_instance_is_on_loan() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.CIRCULATING, true);

    assertThrows(BookAlreadyOnLoanException.class, book::ensureCanBeReserved);
  }

  @Test
  void should_return_true_when_book_instance_is_restricted() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.RESTRICTED, false);

    assertTrue(book.isRestricted());
  }

  @Test
  void should_return_false_when_book_instance_is_not_restricted() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.CIRCULATING, false);

    assertFalse(book.isRestricted());
  }

  @Test
  void should_return_true_when_book_instance_is_on_loan() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.RESTRICTED, true);

    assertTrue(book.isOnLoan());
  }

  @Test
  void should_return_false_when_book_instance_is_not_on_loan() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.CIRCULATING, false);

    assertFalse(book.isOnLoan());
  }
}
