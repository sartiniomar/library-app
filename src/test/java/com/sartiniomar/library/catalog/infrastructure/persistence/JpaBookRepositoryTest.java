package com.sartiniomar.library.catalog.infrastructure.persistence;

import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter.SpringDataBookRepository;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.JpaBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class JpaBookRepositoryTest {

  @Autowired
  private SpringDataBookRepository jpaRepo;

  private JpaBookRepository repository;

  @BeforeEach
  void setup() {
    repository = new JpaBookRepository(jpaRepo);
  }

  @Test
  void shouldSaveAndFindBookById() {

    Book book = Book.create(
        "Title",
        "Author",
        "123"
    );

    Book saved = repository.save(book);

    Optional<Book> found = repository.findById(saved.getId());

    assertTrue(found.isPresent());
    assertEquals("Title", found.get().getTitle());
  }

  @Test
  void shouldDeleteBook() {
    Book book = Book.create(
        "Title",
        "Author",
        "123"
    );

    Book saved = repository.save(book);

    repository.delete(saved.getId());

    Optional<Book> found = repository.findById(saved.getId());
    assertTrue(found.isEmpty());
  }

  @Test
  void shouldExistByIsbn() {
    Book book = Book.create(
        "Title",
        "Author",
        "123"
    );

    repository.save(book);

    assertTrue(repository.existsByIsbn("123"));
  }

  @Test
  void shouldFindByIsbn() {
    Book book = Book.create(
        "Title",
        "Author",
        "123"
    );

    repository.save(book);

    Optional<Book> found = repository.findByIsbn("123");
    assertTrue(found.isPresent());
  }
}
