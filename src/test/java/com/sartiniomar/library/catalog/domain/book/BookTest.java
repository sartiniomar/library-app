package com.sartiniomar.library.catalog.domain.book;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

  private static Stream<Arguments> provideDataForGroupBadRequest() {
    return Stream.of(
        Arguments.of("", "Author", "123"),
        Arguments.of("Title", "", "123"),
        Arguments.of("Title", "Author", ""),
        Arguments.of(null, "Author", "123"),
        Arguments.of("Title", null, "123"),
        Arguments.of("Title", "Author", null)
    );
  }

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

  @ParameterizedTest
  @MethodSource("provideDataForGroupBadRequest")
  @SneakyThrows
  void shouldNotAllowCreateBookWithEmptyRequiredData(String title, String author, String isbn) {
    assertThrows(IllegalArgumentException.class, () ->
        Book.create(title, author, isbn)
    );
  }
}
