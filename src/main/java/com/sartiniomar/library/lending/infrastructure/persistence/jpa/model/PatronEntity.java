package com.sartiniomar.library.lending.infrastructure.persistence.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import java.util.UUID;

@Entity
@Table(name = "patron")
@Getter
public class PatronEntity {

  @Id
  private UUID id;

  private String type;

  protected PatronEntity() {}

  public PatronEntity(UUID id, String type) {
    this.id = id;
    this.type = type;
  }
}
