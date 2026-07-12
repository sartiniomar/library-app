package com.sartiniomar.library.lending.infrastructure.persistence.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "hold")
@Data
public class HoldEntity {

  @Id
  private UUID id;

  private UUID bookInstanceId;

  private UUID patronId;

  protected HoldEntity() {}

  public HoldEntity(UUID id, UUID bookInstanceId, UUID patronId) {
    this.id = id;
    this.bookInstanceId = bookInstanceId;
    this.patronId = patronId;
  }
}