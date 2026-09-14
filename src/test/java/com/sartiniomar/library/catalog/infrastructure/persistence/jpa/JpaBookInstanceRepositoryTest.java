package com.sartiniomar.library.catalog.infrastructure.persistence.jpa;

import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapperImpl;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookMapper;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookMapperImpl;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter.BookAdapterRepository;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter.BookInstanceAdapterRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookJpaRepository;
import com.sartiniomar.library.catalog.support.builder.BookInstanceTestDataBuilder;
import com.sartiniomar.library.catalog.support.builder.BookTestDataBuilder;
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
@Import({
    BookInstanceMapperImpl.class,
    BookMapperImpl.class,
    BookInstanceAdapterRepository.class,
    BookAdapterRepository.class})
class JpaBookInstanceRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BookInstanceJpaRepository bookInstanceJpaRepository;

  @Autowired
  private BookJpaRepository bookJpaRepository;

  @Autowired
  private BookInstanceMapper mapper;

  @Autowired
  private BookMapper bookMapper;

  private BookInstanceAdapterRepository bookInstanceAdapterRepository;

  private BookAdapterRepository bookAdapterRepository;

  @BeforeEach
  void setup() {
    bookInstanceAdapterRepository = new BookInstanceAdapterRepository(bookJpaRepository, bookInstanceJpaRepository, mapper);
    bookAdapterRepository = new BookAdapterRepository(bookJpaRepository, bookMapper);
  }

  @Test
  void givenNewBook_whenSave_thenCanFind() {
    Book book = bookAdapterRepository.save(new BookTestDataBuilder().buildDefault());
    BookInstance saved = bookInstanceAdapterRepository.save(
        new BookInstanceTestDataBuilder().build(book.getId(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));
    assertNotNull(saved.getId());

    entityManager.flush();
    entityManager.clear();

    BookInstance found = bookInstanceAdapterRepository.findById(saved.getId()).orElseThrow();
    assertAll(
        () -> assertEquals(saved.getId(), found.getId()),
        () -> assertEquals(saved.getBookId(), found.getBookId()),
        () -> assertEquals(BookType.CIRCULATING, found.getType()),
        () -> assertEquals(BookInstanceStatus.AVAILABLE, found.getStatus())
    );
  }

  @Test
  void givenExistingBook_whenUpdate_thenPersisted() {
    Book book = bookAdapterRepository.save(new BookTestDataBuilder().buildDefault());
    BookInstance saved = bookInstanceAdapterRepository.save(
        new BookInstanceTestDataBuilder().build(book.getId(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));

    entityManager.flush();
    entityManager.clear();

    BookInstance toUpdate = bookInstanceAdapterRepository.findById(saved.getId()).orElseThrow();
    toUpdate.setType(BookType.RESTRICTED);
    toUpdate.setStatus(BookInstanceStatus.UNAVAILABLE);

    bookInstanceAdapterRepository.save(toUpdate);

    entityManager.flush();
    entityManager.clear();

    BookInstance updated = bookInstanceAdapterRepository.findById(saved.getId()).orElseThrow();
    assertAll(
        () -> assertEquals(BookType.RESTRICTED, updated.getType()),
        () -> assertEquals(BookInstanceStatus.UNAVAILABLE, updated.getStatus())
    );
  }

  @Test
  void givenManyInstances_whenFindAllByBookId_thenReturnsOnlyMatches() {
    Book book1 = bookAdapterRepository.save(new BookTestDataBuilder().buildDefault());
    Book book2 = bookAdapterRepository.save(
        new BookTestDataBuilder().build("Other Title", "Other Author", "Other Isbn"));

    BookInstance a = bookInstanceAdapterRepository.save(
        new BookInstanceTestDataBuilder().build(book1.getId(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));
    BookInstance b = bookInstanceAdapterRepository.save(
        new BookInstanceTestDataBuilder().build(book1.getId(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));
    BookInstance c = bookInstanceAdapterRepository.save(
        new BookInstanceTestDataBuilder().build(book2.getId(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));

    entityManager.flush();
    entityManager.clear();

    List<BookInstance> found = bookInstanceAdapterRepository.findAllByBookId(book1.getId());

    List<UUID> ids = found.stream().map(BookInstance::getId).toList();
    assertTrue(ids.contains(a.getId()));
    assertTrue(ids.contains(b.getId()));
    assertFalse(ids.contains(c.getId()));
    assertEquals(2, found.size());
    assertEquals(book1.getId(), found.getFirst().getBookId());
    assertEquals(BookType.CIRCULATING, found.getFirst().getType());
    assertEquals(BookInstanceStatus.AVAILABLE, found.getFirst().getStatus());
    assertEquals(book1.getId(), found.get(1).getBookId());
    assertEquals(BookType.CIRCULATING, found.get(1).getType());
    assertEquals(BookInstanceStatus.AVAILABLE, found.get(1).getStatus());
  }

  @Test
  void givenExistingBook_whenDelete_thenNotFound() {
    Book book = bookAdapterRepository.save(new BookTestDataBuilder().buildDefault());
    BookInstance saved = bookInstanceAdapterRepository.save(
        new BookInstanceTestDataBuilder().build(book.getId(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE));
    assertNotNull(saved.getId());

    entityManager.flush();
    entityManager.clear();

    assertTrue(bookInstanceAdapterRepository.findById(saved.getId()).isPresent());

    bookInstanceAdapterRepository.delete(saved.getId());

    entityManager.flush();
    entityManager.clear();

    assertFalse(bookInstanceAdapterRepository.findById(saved.getId()).isPresent());
  }
}