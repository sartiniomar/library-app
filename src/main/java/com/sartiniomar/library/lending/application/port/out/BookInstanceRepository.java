package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.domain.book.BookInstance;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceRepository {
  BookInstance save(BookInstance book);
  Optional<BookInstance> findById(UUID bookInstanceId);
  List<BookInstance> findAllByBookId(UUID bookId);
  void delete(UUID id);
}
