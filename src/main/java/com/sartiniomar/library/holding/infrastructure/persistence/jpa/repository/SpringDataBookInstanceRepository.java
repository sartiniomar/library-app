package com.sartiniomar.library.holding.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.holding.infrastructure.persistence.jpa.model.BookInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataBookInstanceRepository extends JpaRepository<BookInstanceEntity, UUID> {
}
