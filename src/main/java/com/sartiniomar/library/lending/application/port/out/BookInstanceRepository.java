package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.domain.book.BookInstance;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceRepository {
  Optional<BookInstance> findById(UUID bookId);
  void save(BookInstance book);
}
