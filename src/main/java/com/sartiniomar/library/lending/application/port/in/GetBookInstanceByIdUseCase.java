package com.sartiniomar.library.lending.application.port.in;

import com.sartiniomar.library.lending.domain.book.BookInstance;
import java.util.UUID;

public interface GetBookInstanceByIdUseCase {
  BookInstance execute(UUID command);
}
