package com.sartiniomar.library.catalog.infrastructure.web.integration;

import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookResponse;
import com.sartiniomar.library.catalog.infrastructure.web.integration.support.factory.BookTestFactory;
import com.sartiniomar.library.catalog.infrastructure.web.integration.support.helper.BookHttpHelper;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.jdbc.JdbcTestUtils;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookIntegrationTest extends BookHttpHelper {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  protected BookTestFactory bookFactory;

  @Test
  void shouldCreateBook() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book");

    BookResponse response = createBook();
    flushAndClear();

    Book book = bookRepository.findById(response.id()).orElseThrow();

    assertThat(book)
        .extracting("title", "author", "isbn")
        .containsExactly("Title", "Author", "123");

    assertEquals(book.getId(), response.id());
    assertEquals(book.getTitle(), response.title());
    assertEquals(book.getAuthor(), response.author());
    assertEquals(book.getIsbn(), response.isbn());

    assertEquals(initialCount + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "book"));
  }

  @Test
  void shouldReturnConflictForDuplicateIsbnInCreate() throws Exception {
    Book book = bookFactory.createDefault();
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book");

    ErrorResponse response = createBookDuplicateIsbn();

    assertEquals("409 CONFLICT", response.code());
    assertEquals("ISBN " + book.getIsbn() + " already exists", response.errors().getFirst().description());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "book"));
  }

  @Test
  void shouldUpdateBook() throws Exception {
    Book book = bookFactory.createDefault();
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book");

    BookResponse response = updateBook(book.getId().toString());
    flushAndClear();

    Book bookUpdated = bookRepository.findById(response.id()).orElseThrow();

    assertThat(bookUpdated)
        .extracting("title", "author", "isbn")
        .containsExactly("Other Title", "Other Author", "Other 123");

    assertEquals(bookUpdated.getId(), response.id());
    assertEquals(bookUpdated.getTitle(), response.title());
    assertEquals(bookUpdated.getAuthor(), response.author());
    assertEquals(bookUpdated.getIsbn(), response.isbn());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "book"));
  }

  @Test
  void shouldReturnConflictForDuplicateIsbnInUpdate() throws Exception {
    Book book = bookFactory.create("Other Title", "Other Author", "Other 123");
    Book bookToUpdate = bookFactory.createDefault();
    flushAndClear();

    ErrorResponse response = updateBookDuplicateIsbn(bookToUpdate.getId());
    flushAndClear();

    assertEquals("409 CONFLICT", response.code());
    assertEquals("ISBN " + book.getIsbn() + " already exists", response.errors().getFirst().description());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnUpdate() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = updateBookNotFound(id);
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book not found with id: " + id, response.errors().getFirst().description());
  }

  @Test
  void shouldGetById() throws Exception {
    BookResponse book = createBook();
    flushAndClear();

    BookResponse response = getById(book.id().toString());

    assertEquals(book.id(), response.id());
    assertEquals(book.title(), response.title());
    assertEquals(book.author(), response.author());
    assertEquals(book.isbn(), response.isbn());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnGetById() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = updateBookNotFound(id);
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book not found with id: " + id, response.errors().getFirst().description());
  }

  @Test
  void shouldGetByIsbn() throws Exception {
    BookResponse book = createBook();
    flushAndClear();

    BookResponse response = getByIsbn(book.isbn());

    assertEquals(book.id(), response.id());
    assertEquals(book.title(), response.title());
    assertEquals(book.author(), response.author());
    assertEquals(book.isbn(), response.isbn());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnGetByIsbn() throws Exception {
    UUID isbn = UUID.randomUUID();
    ErrorResponse response = getByIsbnNotFound(isbn.toString());
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book with ISBN " + isbn + " not found", response.errors().getFirst().description());
  }

  @Test
  void shouldDeleteBook() throws Exception {
    BookResponse response = createBook();
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book");

    deleteBook(response.id().toString());
    flushAndClear();

    assertEquals(initialCount - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "book"));
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnDelete() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = deleteBookNotFound(id.toString());
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book not found with id: " + id, response.errors().getFirst().description());
  }
}
