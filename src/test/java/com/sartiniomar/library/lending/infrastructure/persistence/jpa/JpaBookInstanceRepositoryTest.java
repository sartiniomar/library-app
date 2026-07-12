package com.sartiniomar.library.lending.infrastructure.persistence.jpa;

import com.sartiniomar.library.lending.domain.book.BookType;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceMapperImpl;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter.BookInstanceJpaRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository.BookInstanceSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(BookInstanceMapperImpl.class)
class JpaBookInstanceRepositoryTest {

  @Autowired
  private TestEntityManager em;

  @Autowired
  private BookInstanceSpringDataRepository jpaRepo;

  @Autowired
  private BookInstanceMapper mapper;

  private BookInstanceJpaRepository repository;

  @BeforeEach
  void setup() {
    repository = new BookInstanceJpaRepository(jpaRepo, mapper);
  }

  @Test
  void givenNewBook_whenSave_thenCanFind() {
    UUID bookId = UUID.randomUUID();
    BookInstance toSave = BookInstance.circulating(bookId);

    BookInstance saved = repository.save(toSave);
    assertNotNull(saved.getId());

    em.flush();
    em.clear();

    BookInstance found = repository.findById(saved.getId()).orElseThrow();
    assertAll(
        () -> assertEquals(BookType.CIRCULATING, found.getType()),
        () -> assertFalse(found.isOnHold()),
        () -> assertEquals(bookId, found.getBookId())
    );
  }

  @Test
  void givenExistingBook_whenUpdate_thenPersisted() {
    UUID bookId = UUID.randomUUID();
    BookInstance saved = repository.save(BookInstance.circulating(bookId));

    em.flush();
    em.clear();

    BookInstance toUpdate = new BookInstance(saved.getId(), saved.getBookId(), BookType.RESTRICTED, true);

    repository.save(toUpdate);

    em.flush();
    em.clear();

    BookInstance updated = repository.findById(saved.getId()).orElseThrow();
    assertAll(
        () -> assertEquals(BookType.RESTRICTED, updated.getType()),
        () -> assertTrue(updated.isOnHold())
    );
  }

  @Test
  void givenManyInstances_whenFindAllByBookId_thenReturnsOnlyMatches() {
    UUID bookId = UUID.randomUUID();
    BookInstance a = repository.save(BookInstance.circulating(bookId));
    BookInstance b = repository.save(BookInstance.circulating(bookId));
    repository.save(BookInstance.circulating(UUID.randomUUID()));

    em.flush();
    em.clear();

    List<BookInstance> found = repository.findAllByBookId(bookId);
    assertEquals(2, found.size());
    List<UUID> ids = found.stream().map(BookInstance::getId).toList();
    assertTrue(ids.contains(a.getId()));
    assertTrue(ids.contains(b.getId()));
  }

  @Test
  void givenExistingBook_whenDelete_thenNotFound() {
    UUID bookId = UUID.randomUUID();
    BookInstance saved = repository.save(BookInstance.circulating(bookId));
    assertNotNull(saved.getId());

    em.flush();
    em.clear();

    assertTrue(repository.findById(saved.getId()).isPresent());

    repository.delete(saved.getId());

    em.flush();
    em.clear();

    assertFalse(repository.findById(saved.getId()).isPresent());
  }
}