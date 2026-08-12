package com.sartiniomar.library.catalog.infrastructure.web.integration;

import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookInstanceResponse;
import com.sartiniomar.library.catalog.infrastructure.web.integration.support.factory.BookInstanceTestFactory;
import com.sartiniomar.library.catalog.infrastructure.web.integration.support.factory.BookTestFactory;
import com.sartiniomar.library.catalog.infrastructure.web.integration.support.helper.BookInstanceHttpHelper;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.jdbc.JdbcTestUtils;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookInstanceControllerIntegrationTest extends BookInstanceHttpHelper {

  @Autowired
  private BookInstanceRepository bookInstanceRepository;

  @Autowired
  private BookTestFactory bookFactory;

  @Autowired
  private BookInstanceTestFactory bookInstanceFactory;

  @Test
  void shouldCreateCirculatingBookInstance() throws Exception {
    Book book = bookFactory.createDefault();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book_instance");

    BookInstanceResponse response = createBookInstanceCirculating(book.getId());
    flushAndClear();

    BookInstance bookInstance = bookInstanceRepository.findById(response.id()).orElseThrow();

    assertThat(bookInstance)
        .extracting("bookId", "type", "onHold")
        .containsExactly(book.getId(), BookType.CIRCULATING, false);

    assertEquals(book.getId(), response.bookId());
    assertEquals(BookType.CIRCULATING, response.type());
    assertEquals(false, response.onHold());

    assertEquals(initialCount + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "book_instance"));
  }

  @Test
  void shouldReturnNotFoundWhenBookIdNotFoundInCirculatingBookInstanceCreation() throws Exception {
    UUID bookId = UUID.randomUUID();
    ErrorResponse response = createBookInstanceCirculatingNotFound(bookId);

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book not found with id: " + bookId, response.errors().get(0).description());
  }

  @Test
  void shouldCreateRestrictedBookInstance() throws Exception {
    Book book = bookFactory.createDefault();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book_instance");

    BookInstanceResponse response = createBookInstanceRestricted(book.getId());
    flushAndClear();

    BookInstance bookInstance = bookInstanceRepository.findById(response.id()).orElseThrow();

    assertThat(bookInstance)
        .extracting("bookId", "type", "onHold")
        .containsExactly(book.getId(), BookType.RESTRICTED, false);

    assertEquals(book.getId(), response.bookId());
    assertEquals(BookType.RESTRICTED, response.type());
    assertEquals(false, response.onHold());

    assertEquals(initialCount + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "book_instance"));
  }

  @Test
  void shouldReturnNotFoundWhenBookIdNotFoundInRestrictedBookInstanceCreation() throws Exception {
    UUID bookId = UUID.randomUUID();
    ErrorResponse response = createBookInstanceRestrictedNotFound(bookId);

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book not found with id: " + bookId, response.errors().get(0).description());
  }

  @Test
  void shouldUpdateBookInstance() throws Exception {
    BookInstance bookInstance = bookInstanceFactory.createCirculatingDefault();
    flushAndClear();

    BookInstanceResponse response = updateBookInstanceCirculating(bookInstance.getId(), bookInstance.getBookId());
    flushAndClear();

    BookInstance bookInstanceBBDD = bookInstanceRepository.findById(response.id()).orElseThrow();

    assertThat(bookInstanceBBDD)
        .extracting("bookId", "type", "onHold")
        .containsExactly(bookInstance.getBookId(), BookType.RESTRICTED, true);

    assertEquals(bookInstance.getId(), response.id());
    assertEquals(bookInstance.getBookId(), response.bookId());
    assertEquals(BookType.RESTRICTED, response.type());
    assertEquals(true, response.onHold());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookInstanceOnUpdate() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = updateBookInstanceCirculatingNotFound(id, UUID.randomUUID());

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found with id: " + id, response.errors().get(0).description());
  }

  @Test
  void shouldGetById() throws Exception {
    BookInstance bookInstance = bookInstanceFactory.createCirculatingDefault();
    flushAndClear();

    BookInstanceResponse response = getBookInstanceById(bookInstance.getId());

    assertEquals(bookInstance.getId(), response.id());
    assertEquals(bookInstance.getBookId(), response.bookId());
    assertEquals(bookInstance.getType(), response.type());
    assertEquals(bookInstance.isOnHold(), response.onHold());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookInstanceOnGetById() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = getBookInstanceByIdNotFound(id);

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found with id: " + id, response.errors().get(0).description());
  }

  @Test
  void shouldGetAllBookInstancesByBookId() throws Exception {
    Book book = bookFactory.createDefault();
    BookInstance bookInstance1 = bookInstanceFactory.createCirculating(book.getId(), false);
    BookInstance bookInstance2 = bookInstanceFactory.createRestricted(book.getId(), false);
    flushAndClear();

    List<BookInstanceResponse> response = getAllBookInstancesByBookId(book.getId());

    assertEquals(2, response.size());
    assertEquals(bookInstance1.getId(), response.getFirst().id());
    assertEquals(bookInstance1.getBookId(), response.getFirst().bookId());
    assertEquals(bookInstance1.getType(), response.getFirst().type());
    assertEquals(bookInstance1.isOnHold(), response.getFirst().onHold());
    assertEquals(bookInstance2.getId(), response.get(1).id());
    assertEquals(bookInstance2.getBookId(), response.get(1).bookId());
    assertEquals(bookInstance2.getType(), response.get(1).type());
    assertEquals(bookInstance2.isOnHold(), response.get(1).onHold());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnGetAllById() throws Exception {
    UUID bookId = UUID.randomUUID();
    ErrorResponse response = getAllBookInstancesByBookIdNotFound(bookId);

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book not found with id: " + bookId, response.errors().getFirst().description());
  }

  @Test
  void shouldDeleteBookInstance() throws Exception {
    Book book = bookFactory.createDefault();
    BookInstance bookInstance = bookInstanceFactory.createCirculating(book.getId(), false);
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book_instance");

    deleteBookInstance(book.getId(), bookInstance.getId());

    flushAndClear();

    assertEquals(initialCount - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "book_instance"));
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookInstanceOnDelete() {
    UUID id = UUID.randomUUID();
    ErrorResponse response = deleteBookInstanceNotFound(UUID.randomUUID(), id);

    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Book Instance not found with id: " + id, response.errors().getFirst().description());
  }
}
