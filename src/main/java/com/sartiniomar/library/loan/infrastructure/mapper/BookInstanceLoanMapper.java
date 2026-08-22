package com.sartiniomar.library.loan.infrastructure.mapper;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookInstanceLoanMapper {

  BookInstance toDomain(BookInstanceEntity bookInstanceEntity);

  BookInstanceEntity toEntity(BookInstance bookInstance);

}
