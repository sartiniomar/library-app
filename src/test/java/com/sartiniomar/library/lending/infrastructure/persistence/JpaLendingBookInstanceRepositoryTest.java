package com.sartiniomar.library.lending.infrastructure.persistence;

import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.lending.domain.bookInstance.BookInstance;
import com.sartiniomar.library.lending.domain.bookInstance.BookType;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter.LendingBookInstanceAdapterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(LendingBookInstanceAdapterRepository.class)
public class JpaLendingBookInstanceRepositoryTest {

  @Autowired
  private LendingBookInstanceAdapterRepository repository;

  @Autowired
  private BookInstanceJpaRepository bookInstanceDataRepository;

  @Test
  void shouldSaveAndFindBookInstance() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstanceEntity entity = new BookInstanceEntity();
    entity.setId(id);
    entity.setBookId(bookId);
    entity.setType(com.sartiniomar.library.catalog.domain.bookInstance.BookType.CIRCULATING);
    entity.setOnHold(false);

    bookInstanceDataRepository.save(entity);

    Optional<BookInstance> result = repository.findById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
    assertEquals(bookId, result.get().getBookId());
    assertEquals(BookType.CIRCULATING, result.get().getType());
    assertFalse(result.get().isOnHold());
  }

}
