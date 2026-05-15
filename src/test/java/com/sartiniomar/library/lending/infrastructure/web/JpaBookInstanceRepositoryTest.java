package com.sartiniomar.library.lending.infrastructure.web;

import com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter.JpaBookInstanceInstanceRepository;
import com.sartiniomar.library.lending.model.book.BookInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaBookInstanceInstanceRepository.class)
class JpaBookInstanceRepositoryTest {

  @Autowired
  private JpaBookInstanceInstanceRepository repository;

  @Test
  void shouldSaveAndFindBook() {
    BookInstance book = BookInstance.circulating("book-1");

    repository.save(book);

    Optional<BookInstance> result = repository.findById(book.getId());

    assertTrue(result.isPresent());
  }
}