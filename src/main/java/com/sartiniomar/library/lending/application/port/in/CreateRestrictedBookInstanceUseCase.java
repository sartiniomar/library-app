package com.sartiniomar.library.lending.application.port.in;

import com.sartiniomar.library.lending.domain.book.BookInstance;

public interface CreateRestrictedBookInstanceUseCase {
  BookInstance execute(CreateBookInstanceCommand command);
}
