package com.sartiniomar.library.patron.infrastructure.persistence.jpa.mapper;

import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.model.PatronEntity;

public class PatronMapper {

  public static Patron toDomain(PatronEntity entity) {
    return new Patron(
        entity.getId(),
        PatronType.valueOf(entity.getType()),
        entity.getName(),
        entity.getEmail()
    );
  }

  public static PatronEntity toEntity(Patron patron) {
    PatronEntity entity = new PatronEntity();
    entity.setId(patron.getId());
    entity.setType(patron.getType().toString());
    entity.setName(patron.getName());
    entity.setEmail(patron.getEmail());
    return entity;
  }
}
