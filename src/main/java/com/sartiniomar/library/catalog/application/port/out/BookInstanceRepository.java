package com.sartiniomar.library.catalog.application.port.out;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceRepository {
  BookInstance save(BookInstance book);
  Optional<BookInstance> findById(UUID bookInstanceId);
  List<BookInstance> findAllByBookId(UUID bookId);
  void delete(UUID id);
}
