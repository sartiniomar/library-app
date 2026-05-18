package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.mapper.BookMapper;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter.SpringDataBookRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaBookRepository implements BookRepository {

  private final SpringDataBookRepository jpaRepo;

  public JpaBookRepository(SpringDataBookRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public Book save(Book book) {
    return BookMapper.toDomain(
        jpaRepo.save(BookMapper.toEntity(book))
    );
  }

  @Override
  public Optional<Book> findById(UUID id) {
    return jpaRepo.findById(id)
        .map(BookMapper::toDomain);
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
        .map(BookMapper::toDomain);
  }
}
