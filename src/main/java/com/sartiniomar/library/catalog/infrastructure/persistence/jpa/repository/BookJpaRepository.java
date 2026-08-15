package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {
  boolean existsByIsbn(String isbn);
  Optional<BookEntity> findByIsbn(String isbn);
}
