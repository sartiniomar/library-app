package com.sartiniomar.library.catalog.infrastructure.persistence.model;

import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "book_instance")
@Getter
@Setter
public class BookInstanceEntity {
  @Id
  private UUID id;
  private UUID bookId;
  @Enumerated(EnumType.STRING)
  private BookType type;
  private Boolean onLoan;
  @Version
  private Long version;
}