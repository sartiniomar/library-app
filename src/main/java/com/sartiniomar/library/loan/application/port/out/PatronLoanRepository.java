package com.sartiniomar.library.loan.application.port.out;

import com.sartiniomar.library.loan.domain.patron.Patron;
import java.util.Optional;
import java.util.UUID;

public interface PatronLoanRepository {
  Optional<Patron> findById(UUID patronId);
}
