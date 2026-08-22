package com.sartiniomar.library.loan.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, UUID>  {
  long countByPatronId(UUID patronId);
}
