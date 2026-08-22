package com.sartiniomar.library.loan.application.port.out;

import com.sartiniomar.library.loan.domain.loan.Loan;

import java.util.Optional;
import java.util.UUID;

public interface LoanRepository {
  Integer countByPatronId(UUID patronId);
  void save(Loan hold);
  Optional<Loan> findById(UUID id);
}
