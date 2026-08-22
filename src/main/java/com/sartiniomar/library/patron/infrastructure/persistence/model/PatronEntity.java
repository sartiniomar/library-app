package com.sartiniomar.library.patron.infrastructure.persistence.model;

import com.sartiniomar.library.patron.domain.patron.PatronType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  @Enumerated(EnumType.STRING)
  private PatronType type;
  private String name;
  private String email;
}
