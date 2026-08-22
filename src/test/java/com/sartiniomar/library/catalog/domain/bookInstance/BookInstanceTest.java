package com.sartiniomar.library.catalog.domain.bookInstance;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class BookInstanceTest {

  @Test
  void should_create_successfuly_circulating_book_instance() {
    BookInstance book = BookInstance.circulating(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

    assertEquals("123e4567-e89b-12d3-a456-426614174000", book.getBookId().toString());
    assertFalse(book.isOnHold());
    assertEquals(BookType.CIRCULATING, book.getType());
  }

  @Test
  void should_create_successfuly_restricted_book_instance() {
    BookInstance book = BookInstance.restricted(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

    assertEquals("123e4567-e89b-12d3-a456-426614174000", book.getBookId().toString());
    assertFalse(book.isOnHold());
    assertEquals(BookType.RESTRICTED, book.getType());
  }

  @Test
  void should_not_allow_to_place_hold_when_already_on_hold() {
    BookInstance book = BookInstance.circulating(UUID.randomUUID());

    book.markOnHold();

    assertThrows(BookAlreadyOnHoldException.class, book::markOnHold);
  }

  @Test
  void should_update_book_id_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    UUID updatedBookId = UUID.randomUUID();
    bookInstance.update(updatedBookId, null, null);

    assertEquals(updatedBookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertFalse(bookInstance.isOnHold());
  }

  @Test
  void should_update_book_type_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, BookType.RESTRICTED, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertFalse(bookInstance.isOnHold());
  }

  @Test
  void should_update_on_hold_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, null, true);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertTrue(bookInstance.isOnHold());
  }

  @Test
  void should_update_book_id_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    UUID updatedBookId = UUID.randomUUID();
    bookInstance.update(updatedBookId, null, null);

    assertEquals(updatedBookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertFalse(bookInstance.isOnHold());
  }

  @Test
  void should_update_book_type_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, BookType.CIRCULATING, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertFalse(bookInstance.isOnHold());
  }

  @Test
  void should_update_on_hold_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, null, true);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertTrue(bookInstance.isOnHold());
  }
}
