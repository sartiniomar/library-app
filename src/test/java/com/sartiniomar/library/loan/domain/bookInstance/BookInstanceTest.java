package com.sartiniomar.library.loan.domain.bookInstance;

import com.sartiniomar.library.loan.domain.loan.TransitionStatusException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookInstanceTest {

  private static Stream<Arguments> provideDataForGroupReservedChangeStatusError() {
    return Stream.of(
        Arguments.of("LENT"),
        Arguments.of("RESERVED"),
        Arguments.of("UNAVAILABLE")
    );
  }

  private static Stream<Arguments> provideDataForGroupLentChangeStatusError() {
    return Stream.of(
        Arguments.of("LENT"),
        Arguments.of("UNAVAILABLE")
    );
  }

  private static Stream<Arguments> provideDataForGroupAvailableChangeStatus() {
    return Stream.of(
        Arguments.of("RESERVED"),
        Arguments.of("LENT"),
        Arguments.of("UNAVAILABLE")
    );
  }

  private static Stream<Arguments> provideDataForGroupUnavailableChangeStatus() {
    return Stream.of(
        Arguments.of("RESERVED"),
        Arguments.of("LENT"),
        Arguments.of("AVAILABLE")
    );
  }

  @Test
  void should_create_successfully_book_instance() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = new BookInstance(id, bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    assertEquals(id, bookInstance.getId());
    assertEquals(bookId, bookInstance.getBookId());
    assertEquals(BookType.CIRCULATING, bookInstance.getType());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_throw_exception_when_book_instance_is_unavailable() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = new BookInstance(id, bookId, BookType.CIRCULATING, BookInstanceStatus.UNAVAILABLE);

    assertThrows(BookInstanceNotAvailableException.class, bookInstance::ensureCanBeReserved);
  }

  @Test
  void should_return_true_when_book_instance_is_restricted() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.RESTRICTED, BookInstanceStatus.AVAILABLE);

    assertTrue(book.isRestricted());
  }

  @Test
  void should_return_false_when_book_instance_is_not_restricted() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstance book = new BookInstance(id, bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    assertFalse(book.isRestricted());
  }

  @Test
  void should_change_available_to_reserved_status() {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    bookInstance.reserved();
    assertEquals(BookInstanceStatus.RESERVED, bookInstance.getStatus());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupReservedChangeStatusError")
  @SneakyThrows
  void should_throw_exception_when_book_instance_status_is_not_allow_for_reserved(BookInstanceStatus status) {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, status);

    assertThrows(TransitionStatusException.class, bookInstance::reserved);
  }

  @Test
  void should_change_available_to_lent_status() {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);
    bookInstance.lent();
    assertEquals(BookInstanceStatus.LENT, bookInstance.getStatus());
  }

  @Test
  void should_change_reserved_to_lent_status() {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.RESERVED);
    bookInstance.lent();
    assertEquals(BookInstanceStatus.LENT, bookInstance.getStatus());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupLentChangeStatusError")
  @SneakyThrows
  void should_throw_exception_when_book_instance_status_is_not_allow_for_lent(BookInstanceStatus status) {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, status);

    assertThrows(TransitionStatusException.class, bookInstance::lent);
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupAvailableChangeStatus")
  @SneakyThrows
  void should_change_to_available_status(BookInstanceStatus status) {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, status);
    bookInstance.available();
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance.getStatus());
  }

  @Test
  void should_throw_exception_when_book_instance_status_is_not_allow_for_available() {
    BookInstance bookInstance = new BookInstance(UUID.randomUUID(), UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE);

    assertThrows(TransitionStatusException.class, bookInstance::available);
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
