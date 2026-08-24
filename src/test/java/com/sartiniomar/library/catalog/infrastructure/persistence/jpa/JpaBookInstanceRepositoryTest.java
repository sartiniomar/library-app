package com.sartiniomar.library.catalog.infrastructure.persistence.jpa;

import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceStatus;
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
  private TestEntityManager entityManager;

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

    entityManager.flush();
    entityManager.clear();

    BookInstance found = repository.findById(saved.getId()).orElseThrow();
    assertAll(
        () -> assertEquals(saved.getId(), found.getId()),
        () -> assertEquals(saved.getBookId(), found.getBookId()),
        () -> assertEquals(BookType.CIRCULATING, found.getType()),
        () -> assertEquals(BookInstanceStatus.AVAILABLE, found.getStatus())
    );
  }

  @Test
  void givenExistingBook_whenUpdate_thenPersisted() {
    BookInstance saved = repository.save(new BookInstanceTestDataBuilder().buildCirculatingDefault());

    entityManager.flush();
    entityManager.clear();

    BookInstance toUpdate = repository.findById(saved.getId()).orElseThrow();
    UUID newBookId = UUID.randomUUID();
    toUpdate.setBookId(newBookId);
    toUpdate.setType(BookType.RESTRICTED);
    toUpdate.setStatus(BookInstanceStatus.UNAVAILABLE);

    repository.save(toUpdate);

    entityManager.flush();
    entityManager.clear();

    BookInstance updated = repository.findById(saved.getId()).orElseThrow();
    assertAll(
        () -> assertEquals(newBookId, updated.getBookId()),
        () -> assertEquals(BookType.RESTRICTED, updated.getType()),
        () -> assertEquals(BookInstanceStatus.UNAVAILABLE, updated.getStatus())
    );
  }

  @Test
  void givenManyInstances_whenFindAllByBookId_thenReturnsOnlyMatches() {
    UUID bookId = UUID.randomUUID();
    UUID otherBookId = UUID.randomUUID();
    BookInstance a = repository.save(new BookInstanceTestDataBuilder().build(bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));
    BookInstance b = repository.save(new BookInstanceTestDataBuilder().build(bookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));
    BookInstance c = repository.save(new BookInstanceTestDataBuilder().build(otherBookId, BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));

    entityManager.flush();
    entityManager.clear();

    List<BookInstance> found = repository.findAllByBookId(bookId);

    List<UUID> ids = found.stream().map(BookInstance::getId).toList();
    assertTrue(ids.contains(a.getId()));
    assertTrue(ids.contains(b.getId()));
    assertFalse(ids.contains(c.getId()));
    assertEquals(2, found.size());
    assertEquals(bookId, found.getFirst().getBookId());
    assertEquals(BookType.CIRCULATING, found.getFirst().getType());
    assertEquals(BookInstanceStatus.AVAILABLE, found.getFirst().getStatus());
    assertEquals(bookId, found.get(1).getBookId());
    assertEquals(BookType.CIRCULATING, found.get(1).getType());
    assertEquals(BookInstanceStatus.AVAILABLE, found.get(1).getStatus());
  }

  @Test
  void givenExistingBook_whenDelete_thenNotFound() {
    BookInstance saved = repository.save(new BookInstanceTestDataBuilder().buildCirculatingDefault());
    assertNotNull(saved.getId());

    entityManager.flush();
    entityManager.clear();

    assertTrue(repository.findById(saved.getId()).isPresent());

    repository.delete(saved.getId());

    entityManager.flush();
    entityManager.clear();

    assertFalse(repository.findById(saved.getId()).isPresent());
  }
}