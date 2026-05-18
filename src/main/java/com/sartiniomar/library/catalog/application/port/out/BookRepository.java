package com.sartiniomar.library.catalog.application.port.out;

import com.sartiniomar.library.catalog.domain.book.Book;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {
  Book save(Book book);
  boolean existsByIsbn(String isbn);
  Optional<Book> findByIsbn(String isbn);
  Optional<Book> findById(UUID id);
  void delete(UUID id);
}
