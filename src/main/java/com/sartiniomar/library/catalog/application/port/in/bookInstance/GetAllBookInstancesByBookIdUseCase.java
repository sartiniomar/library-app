package com.sartiniomar.library.catalog.application.port.in.bookInstance;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import java.util.List;
import java.util.UUID;

public interface GetAllBookInstancesByBookIdUseCase {
  List<BookInstance> execute(UUID command);
}
