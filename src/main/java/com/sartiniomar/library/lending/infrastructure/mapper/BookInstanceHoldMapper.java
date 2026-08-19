package com.sartiniomar.library.lending.infrastructure.mapper;

import com.sartiniomar.library.lending.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookInstanceHoldMapper {

  BookInstance toDomain(BookInstanceEntity bookInstanceEntity);

  BookInstanceEntity toEntity(BookInstance bookInstance);

}
