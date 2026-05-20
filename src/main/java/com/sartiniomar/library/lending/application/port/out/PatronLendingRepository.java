package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.domain.patron.Patron;
import java.util.Optional;
import java.util.UUID;

public interface PatronLendingRepository {
  Optional<Patron> findById(UUID patronId);
}
