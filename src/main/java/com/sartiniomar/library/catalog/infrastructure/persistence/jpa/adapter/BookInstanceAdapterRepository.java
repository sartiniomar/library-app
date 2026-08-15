package com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapper;
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

  private final BookInstanceJpaRepository jpaRepo;

  private final BookInstanceMapper mapper;

  public BookInstanceAdapterRepository(BookInstanceJpaRepository jpaRepo, BookInstanceMapper bookInstanceMapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = bookInstanceMapper;
  }

  @Override
  @Transactional
  public BookInstance save(BookInstance bookInstance) {
    BookInstanceEntity entity = jpaRepo.findById(bookInstance.getId()).orElse(null);

    if (entity != null) mapper.updateBookInstanceEntityFromBookInstance(bookInstance, entity);
    else entity = mapper.toEntity(bookInstance);

    return mapper.toDomain(jpaRepo.saveAndFlush(entity));
  }

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    return jpaRepo.findById(bookInstanceId)
        .map(mapper::toDomain);
  }

  @Override
  public List<BookInstance> findAllByBookId(UUID bookId) {
    return jpaRepo.findAllByBookId(bookId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(UUID id) {
    jpaRepo.deleteById(id);
  }
}