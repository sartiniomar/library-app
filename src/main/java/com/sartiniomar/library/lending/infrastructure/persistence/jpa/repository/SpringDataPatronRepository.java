package com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.PatronEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataPatronRepository extends JpaRepository<PatronEntity, UUID>  {
}
