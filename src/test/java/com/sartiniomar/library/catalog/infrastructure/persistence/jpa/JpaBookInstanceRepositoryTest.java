package com.sartiniomar.library.catalog.infrastructure.persistence.jpa;

import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapperImpl;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter.BookInstanceAdapterRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.catalog.support.builder.BookInstanceTestDataBuilder;
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
  private BookInstanceJpaRepository jpaRepo;

  @Autowired
  private BookInstanceMapper mapper;

  private BookInstanceAdapterRepository repository;

  @BeforeEach
  void setup() {
    repository = new BookInstanceAdapterRepository(jpaRepo, mapper);
  }

  @Test
  void givenNewBook_whenSave_thenCanFind() {
    BookInstance saved = repository.save(new BookInstanceTestDataBuilder().buildCirculatingDefault());
    assertNotNull(saved.getId());

    em.flush();
    em.clear();

    BookInstance found = repository.findById(saved.getId()).orElseThrow();
    assertAll(
        () -> assertEquals(BookType.CIRCULATING, found.getType()),
        () -> assertFalse(found.isOnHold()),
        () -> assertEquals(saved.getBookId(), found.getBookId())
    );
  }

  @Test
  void givenExistingBook_whenUpdate_thenPersisted() {
    BookInstance saved = repository.save(new BookInstanceTestDataBuilder().buildCirculatingDefault());

    em.flush();
    em.clear();

    BookInstance toUpdate = repository.findById(saved.getId()).orElseThrow();
    toUpdate.setType(BookType.RESTRICTED);
    toUpdate.setOnHold(true);

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
    BookInstance a = repository.save(new BookInstanceTestDataBuilder().build(bookId, BookType.CIRCULATING, false));
    BookInstance b = repository.save(new BookInstanceTestDataBuilder().build(bookId, BookType.CIRCULATING, false));
    repository.save(new BookInstanceTestDataBuilder().build(UUID.randomUUID(), BookType.CIRCULATING, false));

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
    BookInstance saved = repository.save(new BookInstanceTestDataBuilder().buildCirculatingDefault());
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