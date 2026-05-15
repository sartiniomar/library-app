package com.sartiniomar.library.holding.model.patron;

import java.util.UUID;

public class Patron {

  private final UUID id;
  private final PatronType type;

  private static final Integer MAX_VALUE_HOLDS_REGULAR_PATRON = 5;

  private Patron(PatronType type) {
    this.id = UUID.randomUUID();
    this.type = type;
  }

  public static Patron regular() {
    return new Patron(PatronType.REGULAR);
  }

  public static Patron researcher() {
    return new Patron(PatronType.RESEARCHER);
  }

  public UUID getId() {
    return this.id;
  }

  public PatronType getType() {
    return this.type;
  }

  public boolean isResearcher() {
    return this.type == PatronType.RESEARCHER;
  }

  public int maxHolds() {
    return isResearcher() ? Integer.MAX_VALUE : MAX_VALUE_HOLDS_REGULAR_PATRON;
  }
}