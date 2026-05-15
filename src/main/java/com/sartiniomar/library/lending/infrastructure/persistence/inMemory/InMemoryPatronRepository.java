package com.sartiniomar.library.lending.infrastructure.persistence.inMemory;

import com.sartiniomar.library.lending.application.port.out.PatronRepository;
import com.sartiniomar.library.lending.model.patron.Patron;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatronRepository implements PatronRepository {

  private final Map<UUID, Patron> storage = new ConcurrentHashMap<>();

  @Override
  public Optional<Patron> findById(UUID id) {
    return Optional.ofNullable(storage.getOrDefault(id, Patron.regular()));
  }

  @Override
  public void save(Patron patron) {
    storage.put(patron.getId(), patron);
  }

}
