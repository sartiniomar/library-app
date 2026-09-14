package com.sartiniomar.library.catalog.infrastructure.mapper;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookEntity;
import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookInstanceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BookInstanceMapper {

  BookInstanceResponse bookInstanceToBookInstanceResponse(BookInstance bookInstance);

  @Mapping(target = "bookId", source = "book.id")
  BookInstance toDomain(BookInstanceEntity bookInstanceEntity);

  @Mapping(target = "book", source = "bookId")
  BookInstanceEntity toEntity(BookInstance bookInstance);

  void updateBookInstanceEntityFromBookInstance(
      BookInstance bookInstance,
      @MappingTarget BookInstanceEntity entity
  );

  default BookEntity mapBook(UUID bookId) {
    if (bookId == null) {
      return null;
    }

    BookEntity book = new BookEntity();
    book.setId(bookId);
    return book;
  }
}