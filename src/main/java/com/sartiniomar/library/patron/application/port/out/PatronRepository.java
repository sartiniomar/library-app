package com.sartiniomar.library.patron.application.port.out;

import com.sartiniomar.library.patron.domain.patron.Patron;
import java.util.Optional;
import java.util.UUID;

public interface PatronRepository {
  Patron save(Patron patron);
  Optional<Patron> findById(UUID id);
  boolean existsByEmail(String email);
  void delete(UUID id);
  Optional<Patron> findByEmail(String email);
}
