package com.sartiniomar.library.lending.infrastructure.persistence.inMemory;

import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookInstanceRepository implements BookInstanceRepository {

  private final Map<UUID, BookInstance> storage = new ConcurrentHashMap<>();

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    return Optional.ofNullable(storage.getOrDefault(bookInstanceId, BookInstance.circulating(bookInstanceId)));
  }

  @Override
  public Iterable<BookInstance> findAllByBookId(UUID bookId) {
    return null;
  }

  @Override
  public void delete(UUID id) {

  }

  @Override
  public void save(BookInstance book) {
    storage.put(book.getId(), book);
  }

}
