package com.sartiniomar.library.catalog.infrastructure.web.integration.support.factory;

import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.catalog.support.builder.BookInstanceTestDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookInstanceTestFactory {

  @Autowired
      private BookInstanceRepository bookInstanceRepository;

  public BookInstance createCirculatingDefault() {
    return bookInstanceRepository.save(
        new BookInstanceTestDataBuilder().buildCirculatingDefault()
    );
  }

  public BookInstance createCirculating(UUID bookId, Boolean onHold) {
    return bookInstanceRepository.save(
        new BookInstanceTestDataBuilder().build(bookId, BookType.CIRCULATING, onHold)
    );
  }

  public BookInstance createRestrictedDefault() {
    return bookInstanceRepository.save(
        new BookInstanceTestDataBuilder().buildRestrictedDefault()
    );
  }

  public BookInstance createRestricted(UUID bookId, Boolean onHold) {
    return bookInstanceRepository.save(
        new BookInstanceTestDataBuilder().build(bookId, BookType.RESTRICTED, onHold)
    );
  }
}
