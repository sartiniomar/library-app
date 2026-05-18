package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.mapper;

import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.model.BookEntity;

public class BookMapper {

  public static Book toDomain(BookEntity entity) {
    return new Book(
        entity.getId(),
        entity.getTitle(),
        entity.getAuthor(),
        entity.getIsbn()
    );
  }

  public static BookEntity toEntity(Book book) {
    BookEntity entity = new BookEntity();
    entity.setId(book.getId());
    entity.setTitle(book.getTitle());
    entity.setAuthor(book.getAuthor());
    entity.setIsbn(book.getIsbn());
    return entity;
  }
}
