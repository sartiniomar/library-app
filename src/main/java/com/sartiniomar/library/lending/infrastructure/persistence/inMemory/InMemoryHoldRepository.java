package com.sartiniomar.library.lending.infrastructure.persistence.inMemory;

import com.sartiniomar.library.lending.application.port.out.HoldRepository;
import com.sartiniomar.library.lending.model.hold.Hold;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryHoldRepository implements HoldRepository {

  private final Map<UUID, Hold> storage = new ConcurrentHashMap<>();

  @Override
  public Integer countByPatronId(UUID patronId) {
    return (int) storage.values().stream()
        .filter(hold -> patronId.equals(hold.getPatronId()))
        .count();
  }

  @Override
  public void save(Hold hold) {
    storage.put(hold.getId(), hold);
  }
}
