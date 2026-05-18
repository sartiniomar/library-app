package com.sartiniomar.library.catalog.domain.book;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

  @Test
  void shouldCreateBookWithValidData() {
    Book book = Book.create("Title", "Author", "123");

    assertEquals("Title", book.getTitle());
    assertEquals("Author", book.getAuthor());
    assertEquals("123", book.getIsbn());
  }

  @Test
  void shouldCreateBookWithGivenId() {
    Book book = new Book(UUID.fromString("098c8b8f-085d-4c1a-a0f3-d055e7ffb768"),"Title", "Author", "123");

    assertEquals(UUID.fromString("098c8b8f-085d-4c1a-a0f3-d055e7ffb768"), book.getId());
    assertEquals("Title", book.getTitle());
    assertEquals("Author", book.getAuthor());
    assertEquals("123", book.getIsbn());
  }

  @Test
  void shouldUpdateOnlyProvidedFields() {
    Book book = Book.create("Title",
        "Author",
        "123");

    book.update("New Title", null, null);

    assertEquals("New Title", book.getTitle());
    assertEquals("Author", book.getAuthor());
    assertEquals("123", book.getIsbn());
  }

  @Test
  void shouldNotAllowEmptyTitle() {
    assertThrows(IllegalArgumentException.class, () ->
        Book.create("", "Author", "123")
    );
  }

  @Test
  void shouldNotAllowEmptyAuthor() {
    assertThrows(IllegalArgumentException.class, () ->
        Book.create("Title", "", "123")
    );
  }

  @Test
  void shouldNotAllowEmptyIsbn() {
    assertThrows(IllegalArgumentException.class, () ->
        Book.create("Title", "Author", "")
    );
  }

  @Test
  void shouldNotAllowNullTitle() {
    assertThrows(IllegalArgumentException.class, () ->
        Book.create(null, "Author", "123")
    );
  }

  @Test
  void shouldNotAllowNullAuthor() {
    assertThrows(IllegalArgumentException.class, () ->
        Book.create("Title", null, "123")
    );
  }

  @Test
  void shouldNotAllowNullIsbn() {
    assertThrows(IllegalArgumentException.class, () ->
        Book.create("Title", "Author", null)
    );
  }
}
