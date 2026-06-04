package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.domain.book.BookInstance;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceRepository {
  void save(BookInstance book);
  Optional<BookInstance> findById(UUID bookInstanceId);
  Iterable<BookInstance> findAllByBookId(UUID bookId);
  void delete(UUID id);
}
