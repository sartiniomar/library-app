package com.sartiniomar.library.catalog.application.port.in;

import com.sartiniomar.library.catalog.domain.book.Book;
import java.util.UUID;

public interface GetBookByIdUseCase {
  Book get(UUID id);
}
