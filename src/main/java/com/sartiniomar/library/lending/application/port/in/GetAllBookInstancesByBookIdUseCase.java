package com.sartiniomar.library.lending.application.port.in;

import com.sartiniomar.library.lending.domain.book.BookInstance;
import java.util.List;
import java.util.UUID;

public interface GetAllBookInstancesByBookIdUseCase {
  List<BookInstance> execute(UUID command);
}
