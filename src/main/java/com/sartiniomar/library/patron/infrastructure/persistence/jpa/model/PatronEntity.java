package com.sartiniomar.library.patron.infrastructure.persistence.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "patron")
@Getter
@Setter
public class PatronEntity {
  @Id
  private UUID id;
  private String type;
  private String name;
  private String email;
}
