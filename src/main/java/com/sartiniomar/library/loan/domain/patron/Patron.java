package com.sartiniomar.library.loan.domain.patron;

import java.util.UUID;

public class Patron {

  private final UUID id;
  private final PatronType type;
  private final String name;
  private final String email;

  public static final Integer REGULAR_PATRON_LEND_LIMIT_DAYS = 7;
  public static final Integer RESEARCHER_PATRON_LEND_LIMIT_DAYS = 14;

  public Patron(UUID id, PatronType type, String name, String email) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.email = email;
  }

  public static Patron regular(String name, String email) {
    return new Patron(UUID.randomUUID(), PatronType.REGULAR, name, email);
  }

  public static Patron researcher(String name, String email) {
    return new Patron(UUID.randomUUID(), PatronType.RESEARCHER, name, email);
  }

  public UUID getId() {
    return this.id;
  }

  public PatronType getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  public String getEmail() {
    return this.email;
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