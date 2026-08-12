package com.sartiniomar.library.patron.domain.patron;

import java.util.UUID;

public class Patron {

  private final UUID id;
  private PatronType type;
  private String name;
  private String email;

  public Patron(UUID id, PatronType type, String name, String email) {
    if (type == null) {
      throw new IllegalArgumentException("Type cannot be null");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }

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

  public void update(PatronType type, String name, String email) {
    if (type != null) this.type = type;
    if (name != null) this.name = name;
    if (email != null) this.email = email;
  }

  public UUID getId() {
    return id;
  }

  public PatronType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public boolean isResearcher() {
    return this.type == PatronType.RESEARCHER;
  }
}
