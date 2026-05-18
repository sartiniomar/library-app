package com.sartiniomar.library.catalog.application.port.in;

import com.sartiniomar.library.catalog.domain.book.Book;

public interface UpdateBookUseCase {
  Book update(UpdateBookCommand command);
}
