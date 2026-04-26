package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.model.patron.Patron;
import java.util.Optional;
import java.util.UUID;

public interface PatronRepository {
  Optional<Patron> findById(UUID patronId);
  void save(Patron patron);
}
