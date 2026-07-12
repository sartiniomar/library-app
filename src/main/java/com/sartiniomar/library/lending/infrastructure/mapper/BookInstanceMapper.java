package com.sartiniomar.library.lending.infrastructure.mapper;

import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.BookInstanceEntity;
import com.sartiniomar.library.lending.infrastructure.web.dto.BookInstanceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookInstanceMapper {
  BookInstanceResponse bookInstanceToBookInstanceResponse(BookInstance bookInstance);

  BookInstance toDomain(BookInstanceEntity bookInstanceEntity);

  BookInstanceEntity toEntity(BookInstance bookInstance);

  void updateBookInstanceEntityFromBookInstance(BookInstance bookInstance, @MappingTarget BookInstanceEntity entity);
}
