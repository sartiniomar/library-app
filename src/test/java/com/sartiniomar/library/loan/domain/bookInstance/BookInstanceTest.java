package com.sartiniomar.library.loan.domain.bookInstance;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookInstanceTest {

  @Test
  void should_create_successfully_book_instance() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = new BookInstance(id, bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE, false);

    assertEquals(id, bookInstance.getId());
    assertEquals(bookId, bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_throw_exception_when_book_instance_is_unavailable() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = new BookInstance(id, bookId, BookType.CIRCULATING, BookInstanceStatus.UNAVAILABLE, true);

    assertThrows(BookInstanceNotAvailableException.class, bookInstance::ensureCanBeReserved);
  }

  @Test
  void should_return_true_when_book_instance_is_restricted() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.RESTRICTED, BookInstanceStatus.AVAILABLE, false);

    assertTrue(book.isRestricted());
  }

  @Test
  void should_return_false_when_book_instance_is_not_restricted() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE, false);

    assertFalse(book.isRestricted());
  }
}
