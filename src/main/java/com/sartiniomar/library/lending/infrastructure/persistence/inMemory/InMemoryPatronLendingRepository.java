package com.sartiniomar.library.lending.infrastructure.persistence.inMemory;

import com.sartiniomar.library.lending.application.port.out.PatronLendingRepository;
import com.sartiniomar.library.lending.domain.patron.Patron;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPatronLendingRepository implements PatronLendingRepository {

  private final Map<UUID, Patron> storage = new ConcurrentHashMap<>();

  @Override
  public Optional<Patron> findById(UUID id) {
    return Optional.ofNullable(storage.getOrDefault(id, Patron.regular()));
  }
}
