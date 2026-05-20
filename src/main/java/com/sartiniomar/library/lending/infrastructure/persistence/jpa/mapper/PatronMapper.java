package com.sartiniomar.library.lending.infrastructure.persistence.jpa.mapper;

import com.sartiniomar.library.patron.infrastructure.persistence.jpa.model.PatronEntity;
import com.sartiniomar.library.lending.domain.patron.Patron;

public class PatronMapper {

  public static Patron toDomain(PatronEntity entity) {
    return "RESEARCHER".equals(entity.getType())
        ? Patron.researcher()
        : Patron.regular();
  }
}
