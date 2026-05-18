package com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.HoldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataHoldRepository extends JpaRepository<HoldEntity, UUID>  {
  long countByPatronId(UUID patronId);
}
