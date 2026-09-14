package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookJpaRepository;
import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BookInstanceAdapterRepository implements BookInstanceRepository {

  private final BookJpaRepository bookJpaRepository;

  private final BookInstanceJpaRepository bookInstanceJpaRepository;

  private final BookInstanceMapper mapper;

  public BookInstanceAdapterRepository(BookJpaRepository bookJpaRepository, BookInstanceJpaRepository bookInstanceJpaRepository, BookInstanceMapper bookInstanceMapper) {
    this.bookJpaRepository = bookJpaRepository;
    this.bookInstanceJpaRepository = bookInstanceJpaRepository;
    this.mapper = bookInstanceMapper;
  }

  @Override
  @Transactional
  public BookInstance save(BookInstance bookInstance) {
    BookInstanceEntity bookInstanceEntity;

    Optional<BookInstanceEntity> entityOptional =
        bookInstanceJpaRepository.findById(bookInstance.getId());

    if (entityOptional.isPresent()) {
      bookInstanceEntity = entityOptional.get();
      mapper.updateBookInstanceEntityFromBookInstance(
          bookInstance,
          bookInstanceEntity
      );
    } else {
      bookInstanceEntity = mapper.toEntity(bookInstance);
    }

    bookInstanceEntity.setBook(
        bookJpaRepository.getReferenceById(bookInstance.getBookId())
    );

    return mapper.toDomain(
        bookInstanceJpaRepository.saveAndFlush(bookInstanceEntity)
    );
  }

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    return bookInstanceJpaRepository.findById(bookInstanceId)
        .map(mapper::toDomain);
  }

  @Override
  public List<BookInstance> findAllByBookId(UUID bookId) {
    return bookInstanceJpaRepository.findAllByBookId(bookId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(UUID id) {
    bookInstanceJpaRepository.deleteById(id);
  }
}