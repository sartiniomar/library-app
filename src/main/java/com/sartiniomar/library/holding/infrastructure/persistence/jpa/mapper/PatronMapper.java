package com.sartiniomar.library.holding.infrastructure.persistence.jpa.mapper;

import com.sartiniomar.library.holding.infrastructure.persistence.jpa.model.PatronEntity;
import com.sartiniomar.library.holding.model.patron.Patron;

public class PatronMapper {

  public static PatronEntity toEntity(Patron patron) {
    return new PatronEntity(
        patron.getId(),
        patron.isResearcher() ? "RESEARCHER" : "REGULAR"
    );
  }

  public static Patron toDomain(PatronEntity entity) {
    return "RESEARCHER".equals(entity.getType())
        ? Patron.researcher()
        : Patron.regular();
  }
}
