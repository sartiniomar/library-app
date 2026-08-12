package com.sartiniomar.library.lending.infrastructure.persistence.repository;

import com.sartiniomar.library.lending.infrastructure.persistence.model.HoldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface HoldSpringDataRepository extends JpaRepository<HoldEntity, UUID>  {
  long countByPatronId(UUID patronId);
}
