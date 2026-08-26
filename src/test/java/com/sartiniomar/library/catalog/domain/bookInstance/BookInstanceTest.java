package com.sartiniomar.library.catalog.domain.bookInstance;

import com.sartiniomar.library.loan.domain.loan.TransitionStatusException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookInstanceTest {

  private static Stream<Arguments> provideDataForGroupUnavailableChangeStatus() {
    return Stream.of(
        Arguments.of("RESERVED"),
        Arguments.of("LENT"),
        Arguments.of("AVAILABLE")
    );
  }

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
  void should_update_book_type_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(BookType.RESTRICTED, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_update_book_status_in_circulating_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);

    bookInstance.update(null, BookInstanceStatus.RESERVED);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.RESERVED, bookInstance.getStatus());
  }

  @Test
  void should_update_book_type_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(BookType.CIRCULATING, null);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_update_book_status_in_restricted_book_instance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.restricted(bookId);

    bookInstance.update(null, BookInstanceStatus.RESERVED);

    assertEquals(bookId , bookInstance.getBookId());
    assertEquals(BookType.RESTRICTED, bookInstance.getType());
    assertEquals(BookInstanceStatus.RESERVED, bookInstance.getStatus());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupUnavailableChangeStatus")
  @SneakyThrows
  void should_change_to_unavailable_status(BookInstanceStatus status) {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, status);
    bookInstance.unavailable();
    assertEquals(BookInstanceStatus.UNAVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_throw_exception_when_book_instance_status_is_not_allow_for_unavailable() {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.UNAVAILABLE);

    assertThrows(TransitionStatusException.class, bookInstance::unavailable);
  }
}
