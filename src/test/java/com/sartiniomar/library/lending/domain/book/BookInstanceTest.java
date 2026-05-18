package com.sartiniomar.library.lending.domain.book;

import com.sartiniomar.library.lending.domain.hold.BookAlreadyOnHoldException;
import com.sartiniomar.library.lending.domain.patron.OnlyResearcherCanHoldRestrictedBooksException;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookInstanceTest {

  @Test
  void should_create_successfuly_circulating_book_instance() {
    BookInstance book = BookInstance.circulating("book-1");

    assertEquals("book-1", book.getBookId());
    assertFalse(book.isOnHold());
    assertEquals(BookType.CIRCULATING, book.getType());
  }

  @Test
  void should_create_successfuly_restricted_book_instance() {
    BookInstance book = BookInstance.restricted("book-1");

    assertEquals("book-1", book.getBookId());
    assertFalse(book.isOnHold());
    assertEquals(BookType.RESTRICTED, book.getType());
  }

  @Test
  void should_create_successfuly_restore_book_instance() {
    UUID uuid = UUID.randomUUID();
    BookInstance book = BookInstance.restore(uuid, "book-1", BookType.CIRCULATING, false);

    assertEquals(uuid, book.getId());
    assertEquals("book-1", book.getBookId());
    assertFalse(book.isOnHold());
    assertEquals(BookType.CIRCULATING, book.getType());
  }

  @Test
  void should_not_allow_to_place_hold_when_already_on_hold() {
    BookInstance book = BookInstance.circulating("book-1");

    book.markOnHold();

    assertThrows(BookAlreadyOnHoldException.class, book::markOnHold);
  }

  @Test
  void should_not_allow_to_place_hold_when_already_on_hold_validations() {
    BookInstance book = BookInstance.circulating("book-1");
    Patron patron = Patron.regular();

    book.markOnHold();

    assertThrows(BookAlreadyOnHoldException.class, () ->
        book.ensureCanBePlacedOnHoldBy(patron));
  }

  @Test
  void should_not_allow_to_place_hold_when_is_book_restricted_and_regular_patron() {
    BookInstance book = BookInstance.restricted("book-1");
    Patron patron = Patron.regular();

    assertThrows(OnlyResearcherCanHoldRestrictedBooksException.class, () ->
        book.ensureCanBePlacedOnHoldBy(patron));
  }
}
