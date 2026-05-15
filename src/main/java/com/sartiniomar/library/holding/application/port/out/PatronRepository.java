package com.sartiniomar.library.holding.application.port.out;

import com.sartiniomar.library.holding.model.patron.Patron;
import java.util.Optional;
import java.util.UUID;

public interface PatronRepository {
  Optional<Patron> findById(UUID patronId);
  void save(Patron patron);
}
