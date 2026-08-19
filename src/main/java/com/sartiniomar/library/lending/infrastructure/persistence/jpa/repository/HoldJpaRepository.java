package com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.lending.infrastructure.persistence.model.HoldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface HoldJpaRepository extends JpaRepository<HoldEntity, UUID>  {
  long countByPatronId(UUID patronId);
}
