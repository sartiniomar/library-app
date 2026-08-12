package com.sartiniomar.library.catalog.infrastructure.web.integration.support.factory;

import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.support.builder.BookTestDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookTestFactory {

  @Autowired
  private BookRepository bookRepository;

  public Book createDefault() {
    return bookRepository.save(
        new BookTestDataBuilder().buildDefault()
    );
  }

  public Book create(String title, String author, String isbn) {
    return bookRepository.save(new BookTestDataBuilder().build(title, author, isbn));
  }
}
