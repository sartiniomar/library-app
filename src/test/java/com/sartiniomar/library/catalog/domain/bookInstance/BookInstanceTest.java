package com.sartiniomar.library.catalog.domain.bookInstance;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookInstanceTest {

  @Test
  void should_create_successfully_circulating_book_instance() {
    BookInstance book = BookInstance.circulating(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

    assertEquals("123e4567-e89b-12d3-a456-426614174000", book.getBookId().toString());
    assertEquals(BookInstanceStatus.AVAILABLE, book.getStatus());
    assertFalse(book.isOnLoan());
    assertEquals(BookType.CIRCULATING, book.getType());
  }

  @Test
  void should_create_successfully_restricted_book_instance() {
    BookInstance book = BookInstance.restricted(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

    assertEquals("123e4567-e89b-12d3-a456-426614174000", book.getBookId().toString());
    assertEquals(BookInstanceStatus.AVAILABLE, book.getStatus());
    assertFalse(book.isOnLoan());
    assertEquals(BookType.RESTRICTED, book.getType());
  }

  @Test
  void should_update_book_id_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    UUID updatedBookId = UUID.randomUUID();
    bookInstance.update(updatedBookId, null, null, null);

    assertEquals(updatedBookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertFalse(bookInstance.isOnLoan());
  }

  @Test
  void should_update_book_type_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, BookType.RESTRICTED, null, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertFalse(bookInstance.isOnLoan());
  }

  @Test
  void should_update_book_status_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, null, BookInstanceStatus.RESERVED, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.RESERVED, bookInstance.getStatus());
    assertFalse(bookInstance.isOnLoan());
  }

  @Test
  void should_update_on_hold_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, null, null,true);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertTrue(bookInstance.isOnLoan());
  }

  @Test
  void should_update_book_id_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    UUID updatedBookId = UUID.randomUUID();
    bookInstance.update(updatedBookId, null, null, null);

    assertEquals(updatedBookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertFalse(bookInstance.isOnLoan());
  }

  @Test
  void should_update_book_type_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, BookType.CIRCULATING, null, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertFalse(bookInstance.isOnLoan());
  }

  @Test
  void should_update_book_status_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, null, BookInstanceStatus.RESERVED, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.RESERVED, bookInstance.getStatus());
    assertFalse(bookInstance.isOnLoan());
  }

  @Test
  void should_update_on_hold_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, null, null,true);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertTrue(bookInstance.isOnLoan());
  }
}
