package com.sartiniomar.library.catalog.application.port.in.bookInstance;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;

public interface CreateRestrictedBookInstanceUseCase {
  BookInstance execute(CreateBookInstanceCommand command);
}
