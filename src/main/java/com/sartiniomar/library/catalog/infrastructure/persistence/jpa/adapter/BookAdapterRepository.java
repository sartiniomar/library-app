package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookMapper;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class BookAdapterRepository implements BookRepository {

  private final BookJpaRepository jpaRepo;
  private final BookMapper mapper;

  public BookAdapterRepository(BookJpaRepository jpaRepo, BookMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Book save(Book book) {
    log.debug("Saving book. Book Id= {}", book.getId());
    return mapper.toDomain(jpaRepo.save(mapper.toEntity(book)));
  }

  @Override
  public Optional<Book> findById(UUID id) {
    log.debug("Finding book. Book Id= {}", id);
    return jpaRepo.findById(id).map(mapper::toDomain);
  }

  @Override
  public void delete(UUID id) {
    log.debug("Deleting book. Book Id= {}", id);
    jpaRepo.deleteById(id);
  }

  @Override
  public boolean existsByIsbn(String isbn) {
    log.debug("Existing book. ISBN= {}", isbn);
    return jpaRepo.existsByIsbn(isbn);
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    log.debug("Finding book. ISBN= {}", isbn);
    return jpaRepo.findByIsbn(isbn).map(mapper::toDomain);
  }
}
