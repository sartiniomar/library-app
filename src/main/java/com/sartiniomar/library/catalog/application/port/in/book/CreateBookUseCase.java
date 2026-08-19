package com.sartiniomar.library.catalog.application.port.in.book;

import com.sartiniomar.library.catalog.domain.book.Book;

public interface CreateBookUseCase {
  Book execute(CreateBookCommand command);
}
