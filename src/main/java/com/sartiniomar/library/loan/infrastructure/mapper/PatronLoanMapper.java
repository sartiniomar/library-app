package com.sartiniomar.library.loan.infrastructure.mapper;

import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.model.PatronEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatronLoanMapper {
  Patron toDomain(PatronEntity entity);
  PatronEntity toEntity(Patron patron);
}
