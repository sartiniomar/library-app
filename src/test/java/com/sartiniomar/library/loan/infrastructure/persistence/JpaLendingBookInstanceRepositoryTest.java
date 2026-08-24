package com.sartiniomar.library.loan.infrastructure.persistence;

import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanBookInstanceAdapterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(LoanBookInstanceAdapterRepository.class)
public class JpaLendingBookInstanceRepositoryTest {

  @Autowired
  private LoanBookInstanceAdapterRepository repository;

  @Autowired
  private BookInstanceJpaRepository bookInstanceDataRepository;

  @Test
  void shouldSaveAndFindBookInstance() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();
    BookInstanceEntity entity = new BookInstanceEntity();
    entity.setId(id);
    entity.setBookId(bookId);
    entity.setType(BookType.CIRCULATING);
    entity.setStatus(BookInstanceStatus.AVAILABLE);

    bookInstanceDataRepository.save(entity);

    Optional<BookInstance> result = repository.findById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
    assertEquals(bookId, result.get().getBookId());
    assertEquals(BookType.CIRCULATING.toString(), result.get().getType().toString());
    assertEquals(BookInstanceStatus.AVAILABLE.toString(), result.get().getStatus().toString());
  }

}
