package com.sartiniomar.library.catalog.infrastructure.mapper;

import com.sartiniomar.library.catalog.application.port.in.CreateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.UpdateBookCommand;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.model.BookEntity;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookRequest;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookResponse;
import org.mapstruct.Mapper;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BookMapper {

  CreateBookCommand bookRequestToCreateBookCommand(BookRequest bookRequest);

  BookResponse bookToBookResponse(Book book);

  UpdateBookCommand bookRequestToUpdateBookCommand(BookRequest bookRequest, UUID id);

  Book toDomain(BookEntity entity);

  BookEntity toEntity(Book book);
}
