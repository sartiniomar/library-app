package com.sartiniomar.library.catalog.application.port.in.bookInstance;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;

public interface CreateCirculatingBookInstanceUseCase {
  BookInstance execute(CreateBookInstanceCommand command);
}