package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.model.BookInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookInstanceSpringDataRepository extends JpaRepository<BookInstanceEntity, UUID> {
  List<BookInstanceEntity> findAllByBookId(UUID bookId);
}
