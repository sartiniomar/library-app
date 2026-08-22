package com.sartiniomar.library.catalog.application.port.in.book;

import com.sartiniomar.library.catalog.domain.book.Book;
import java.util.UUID;

public interface GetBookByIdUseCase {
  Book execute(UUID id);
}
