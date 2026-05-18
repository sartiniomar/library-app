package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.domain.hold.Hold;
import java.util.Optional;
import java.util.UUID;

public interface HoldRepository {
  Integer countByPatronId(UUID patronId);
  void save(Hold hold);
  Optional<Hold> findById(UUID id);
}
