package com.sartiniomar.library.catalog.domain.book;

import java.util.UUID;

public class Book {

  private final UUID id;
  private String title;
  private String author;
  private String isbn;

  public Book(UUID id, String title, String author, String isbn) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be empty");
    }
    if (author == null || author.isBlank()) {
      throw new IllegalArgumentException("Author cannot be empty");
    }
    if (isbn == null || isbn.isBlank()) {
      throw new IllegalArgumentException("ISBN cannot be empty");
    }

    this.id = id;
    this.title = title;
    this.author = author;
    this.isbn = isbn;
  }

  public static Book create(String title, String author, String isbn) {
    return new Book(
        UUID.randomUUID(),
        title,
        author,
        isbn
    );
  }

  public void update(String title, String author, String isbn) {
    if (title != null) this.title = title;
    if (author != null) this.author = author;
    if (isbn != null) this.isbn = isbn;
  }

  public UUID getId() { return id; }
  public String getTitle() { return title; }
  public String getAuthor() { return author; }
  public String getIsbn() { return isbn; }
}
