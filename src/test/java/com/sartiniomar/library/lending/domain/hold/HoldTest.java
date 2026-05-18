package com.sartiniomar.library.lending.domain.hold;

import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HoldTest {

  @Test
  void should_create_successfuly_hold() {
    Patron patron = Patron.regular();
    BookInstance book = BookInstance.circulating("book-1");

    Hold hold = new Hold(patron.getId(), book.getId());

    assertNotNull(hold.getId());
    assertEquals(patron.getId(), hold.getPatronId());
    assertEquals(book.getId(), hold.getBookInstanceId());
  }

}
