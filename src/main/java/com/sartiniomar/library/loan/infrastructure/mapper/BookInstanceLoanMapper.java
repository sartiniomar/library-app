package com.sartiniomar.library.loan.infrastructure.mapper;

import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookEntity;
import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BookInstanceLoanMapper {

  @Mapping(target = "bookId", source = "book.id")
  BookInstance toDomain(BookInstanceEntity entity);

  @Mapping(target = "book", source = "bookId")
  BookInstanceEntity toEntity(BookInstance bookInstance);

  @Mapping(target = "book", ignore = true)
  void updateBookInstanceEntityFromBookInstance(
      BookInstance bookInstance,
      @MappingTarget BookInstanceEntity entity
  );

  default BookEntity mapBookInstance(UUID bookId) {
    if (bookId == null) {
      return null;
    }

    BookEntity book = new BookEntity();
    book.setId(bookId);
    return book;
  }
}