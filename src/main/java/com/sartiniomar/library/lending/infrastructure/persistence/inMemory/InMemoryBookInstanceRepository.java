package com.sartiniomar.library.lending.infrastructure.persistence.inMemory;

import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.model.book.BookInstance;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookInstanceRepository implements BookInstanceRepository {

  private final Map<UUID, BookInstance> storage = new ConcurrentHashMap<>();

  @Override
  public Optional<BookInstance> findById(UUID id) {
    return Optional.ofNullable(storage.getOrDefault(id, BookInstance.circulating("book-1")));
  }

  @Override
  public void save(BookInstance book) {
    storage.put(book.getId(), book);
  }

}
