package com.sartiniomar.library.holding.infrastructure.persistence.jpa.mapper;

import com.sartiniomar.library.holding.infrastructure.persistence.jpa.model.BookInstanceEntity;
import com.sartiniomar.library.holding.model.book.BookInstance;

public class BookInstanceMapper {

  public static BookInstance toDomain(BookInstanceEntity entity) {
    return BookInstance.restore(
        entity.getId(),
        entity.getBookId(),
        entity.getType(),
        entity.isOnHold()
    );
  }

  public static BookInstanceEntity toEntity(BookInstance domain) {
    BookInstanceEntity entity = new BookInstanceEntity(
        domain.getId(),
        domain.getBookId(),
        domain.getType(),
        domain.isOnHold()
    );
    entity.setOnHold(domain.isOnHold());
    return entity;
  }

  public static void updateEntity(BookInstanceEntity entity, BookInstance domain) {
    entity.setType(domain.getType());
    entity.setOnHold(domain.isOnHold());
  }
}