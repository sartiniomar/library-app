package com.sartiniomar.library.loan.infrastructure.persistence.inMemory.adapter;

import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PatronInMemoryRepository implements PatronLoanRepository {

  private final Map<UUID, Patron> storage = new ConcurrentHashMap<>();

  @Override
  public Optional<Patron> findById(UUID id) {
    return Optional.ofNullable(storage.getOrDefault(id, Patron.regular()));
  }
}
