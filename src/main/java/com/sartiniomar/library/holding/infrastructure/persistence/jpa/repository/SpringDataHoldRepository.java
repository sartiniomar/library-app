package com.sartiniomar.library.holding.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.holding.infrastructure.persistence.jpa.model.HoldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataHoldRepository extends JpaRepository<HoldEntity, String>  {
  long countByPatronId(UUID patronId);
}
