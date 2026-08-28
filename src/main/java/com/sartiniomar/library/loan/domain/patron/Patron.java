package com.sartiniomar.library.loan.domain.patron;

import java.util.UUID;

public class Patron {

  private final UUID id;
  private final PatronType type;

  public Patron(UUID id, PatronType type) {
    this.id = id;
    this.type = type;
  }

  public UUID getId() {
    return this.id;
  }

  public PatronType getType() {
    return this.type;
  }

  public boolean isRegular() {
    return this.type == PatronType.REGULAR;
  }

  public boolean isResearcher() {
    return this.type == PatronType.RESEARCHER;
  }
}