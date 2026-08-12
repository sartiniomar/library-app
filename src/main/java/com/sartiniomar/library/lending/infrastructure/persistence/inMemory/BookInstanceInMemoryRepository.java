package com.sartiniomar.library.lending.infrastructure.persistence.inMemory;

import com.sartiniomar.library.lending.domain.bookInstance.BookInstance;
import com.sartiniomar.library.lending.application.port.out.BookInstanceLendingRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookInstanceInMemoryRepository implements BookInstanceLendingRepository {

  private final Map<UUID, BookInstance> catalogStorage = new ConcurrentHashMap<>();

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    return Optional.ofNullable(catalogStorage.getOrDefault(bookInstanceId, BookInstance.circulating(bookInstanceId)));
  }
}
