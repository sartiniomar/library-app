package com.sartiniomar.library.catalog.domain.bookInstance;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookInstanceTest {

  @Test
  void should_create_successfully_circulating_book_instance() {
    BookInstance bookInstance = BookInstance.circulating(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

    assertNotNull(bookInstance.getId());
    assertEquals("123e4567-e89b-12d3-a456-426614174000", bookInstance.getBookId().toString());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
  }

  @Test
  void should_create_successfully_restricted_book_instance() {
    BookInstance bookInstance = BookInstance.restricted(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

    assertNotNull(bookInstance.getId());
    assertEquals("123e4567-e89b-12d3-a456-426614174000", bookInstance.getBookId().toString());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
  }

  @Test
  void should_update_book_id_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    UUID updatedBookId = UUID.randomUUID();
    bookInstance.update(updatedBookId, null, null);

    assertEquals(updatedBookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_update_book_type_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, BookType.RESTRICTED, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_update_book_status_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, null, BookInstanceStatus.RESERVED);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.RESERVED, bookInstance.getStatus());
  }

  @Test
  void should_update_book_id_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    UUID updatedBookId = UUID.randomUUID();
    bookInstance.update(updatedBookId, null, null);

    assertEquals(updatedBookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_update_book_type_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, BookType.CIRCULATING, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_update_book_status_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, null, BookInstanceStatus.RESERVED);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.RESERVED, bookInstance.getStatus());
  }
}
