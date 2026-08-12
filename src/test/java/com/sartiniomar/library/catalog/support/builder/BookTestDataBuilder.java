package com.sartiniomar.library.catalog.support.builder;

import com.sartiniomar.library.catalog.domain.book.Book;
import java.util.UUID;

public class BookTestDataBuilder {

  private final UUID id = UUID.randomUUID();

  public Book buildDefault() {return new Book(id, "Title", "Author", "123");}

  public Book build(String title, String author, String isbn) {return new Book(id, title, author, isbn);}
}
