package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookMapper;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookSpringDataRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookJpaRepository implements BookRepository {

  private final BookSpringDataRepository jpaRepo;

  private final BookMapper mapper;

  public BookJpaRepository(BookSpringDataRepository jpaRepo, BookMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Book save(Book book) {
    return mapper.toDomain(
        jpaRepo.save(mapper.toEntity(book))
    );
  }

  @Override
  public Optional<Book> findById(UUID id) {
    return jpaRepo.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public void delete(UUID id) {
    jpaRepo.deleteById(id);
  }

  @Override
  public boolean existsByIsbn(String isbn) {
    return jpaRepo.existsByIsbn(isbn);
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    return jpaRepo.findByIsbn(isbn)
        .map(mapper::toDomain);
  }
}
