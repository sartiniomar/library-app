package com.sartiniomar.library.lending.application.port.in;

import com.sartiniomar.library.lending.domain.book.BookInstance;

public interface UpdateBookInstanceUseCase {
  BookInstance execute(UpdateBookInstanceCommand command);
}
