package com.sartiniomar.library.catalog.application.port.in.bookInstance;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import java.util.UUID;

public interface GetBookInstanceByIdUseCase {
  BookInstance execute(UUID command);
}
