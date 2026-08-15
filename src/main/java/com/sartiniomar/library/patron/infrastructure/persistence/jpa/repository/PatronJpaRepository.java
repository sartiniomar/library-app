package com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.patron.infrastructure.persistence.model.PatronEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PatronJpaRepository extends JpaRepository<PatronEntity, UUID> {

  boolean existsByEmail(String email);

  Optional<PatronEntity> findByEmail(String email);
}
