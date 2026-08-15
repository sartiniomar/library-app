package com.sartiniomar.library.lending.infrastructure.mapper;

import com.sartiniomar.library.lending.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.model.PatronEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatronHoldMapper {
  Patron toDomain(PatronEntity entity);
  PatronEntity toEntity(Patron patron);
}
