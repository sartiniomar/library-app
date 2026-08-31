package com.sartiniomar.library.loan.domain.patron;

import java.util.UUID;

public class Patron {

  private final UUID id;
  private final PatronType type;

  public static final Integer REGULAR_PATRON_LEND_LIMIT_DAYS = 7;
  public static final Integer RESEARCHER_PATRON_LEND_LIMIT_DAYS = 14;

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

  public Integer getLimitDays() {
    return PatronType.REGULAR.equals(this.getType()) ?
        REGULAR_PATRON_LEND_LIMIT_DAYS : RESEARCHER_PATRON_LEND_LIMIT_DAYS;
  }
}