package com.sartiniomar.library.lending.infrastructure.persistence.adapter;

import com.sartiniomar.library.lending.application.port.out.BookInstanceLendingRepository;
import com.sartiniomar.library.lending.domain.bookInstance.BookInstance;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceHoldMapper;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceHoldMapperImpl;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceSpringDataRepository;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Import(BookInstanceHoldMapperImpl.class)
public class LendingBookInstanceJpaRepository implements BookInstanceLendingRepository {

  private final BookInstanceSpringDataRepository jpaRepo;

  private final BookInstanceHoldMapper mapper;

  public LendingBookInstanceJpaRepository(BookInstanceSpringDataRepository jpaRepo, BookInstanceHoldMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    return jpaRepo.findById(bookInstanceId)
        .map(mapper::toDomain);
  }

}
