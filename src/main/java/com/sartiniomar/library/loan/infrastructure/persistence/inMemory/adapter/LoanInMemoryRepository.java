package com.sartiniomar.library.loan.infrastructure.persistence.inMemory.adapter;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.Loan;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LoanInMemoryRepository implements LoanRepository {

  private final Map<UUID, Loan> storage = new ConcurrentHashMap<>();

  @Override
  public Integer countByPatronId(UUID patronId) {
    return (int) storage.values().stream()
        .filter(loan -> patronId.equals(loan.getPatronId()))
        .count();
  }

  @Override
  public void save(Loan loan) {
    storage.put(loan.getId(), loan);
  }

  @Override
  public Optional<Loan> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }
}
