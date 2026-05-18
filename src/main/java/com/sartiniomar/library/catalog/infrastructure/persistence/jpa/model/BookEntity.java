package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter
public class BookEntity {

  @Id
  private UUID id;
  private String title;
  private String author;
  private String isbn;
}
