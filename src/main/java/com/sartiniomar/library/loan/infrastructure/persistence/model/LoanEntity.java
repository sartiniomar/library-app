package com.sartiniomar.library.loan.infrastructure.persistence.model;

import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.patron.infrastructure.persistence.model.PatronEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loan")
@Data
public class LoanEntity {

  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "patron_id", referencedColumnName = "id")
  private PatronEntity patron;

  @ManyToOne
  @JoinColumn(name = "book_instance_id", referencedColumnName = "id")
  private BookInstanceEntity bookInstance;

  @Enumerated(EnumType.STRING)
  private LoanStatus status;

  private Instant reservedAt;
  private Instant lentAt;
  private Instant dueAt;
  private Instant returnedAt;
}