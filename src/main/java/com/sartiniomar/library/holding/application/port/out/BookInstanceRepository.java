package com.sartiniomar.library.holding.application.port.out;

import com.sartiniomar.library.holding.model.book.BookInstance;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceRepository {
  Optional<BookInstance> findById(UUID bookId);
  void save(BookInstance book);
}
