package com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.BookInstanceEntity;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository.BookInstanceSpringDataRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BookInstanceJpaRepository implements BookInstanceRepository {

  private final BookInstanceSpringDataRepository jpaRepo;

  private final BookInstanceMapper mapper;

  public BookInstanceJpaRepository(BookInstanceSpringDataRepository jpaRepo, BookInstanceMapper bookInstanceMapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = bookInstanceMapper;
  }

  @Override
  @Transactional
  public BookInstance save(BookInstance bookInstance) {
    BookInstanceEntity entity = jpaRepo.findById(bookInstance.getId()).orElse(null);

    if (entity != null) {
      mapper.updateBookInstanceEntityFromBookInstance(bookInstance, entity);
    } else {
      entity = mapper.toEntity(bookInstance);
    }
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