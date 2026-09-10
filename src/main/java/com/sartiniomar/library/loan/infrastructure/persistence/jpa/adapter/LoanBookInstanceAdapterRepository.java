package com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.infrastructure.mapper.BookInstanceLoanMapper;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.loan.infrastructure.mapper.BookInstanceLoanMapperImpl;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
@Import(BookInstanceLoanMapperImpl.class)
public class LoanBookInstanceAdapterRepository implements BookInstanceLoanRepository {

  private final BookInstanceJpaRepository jpaRepo;

  private final BookInstanceLoanMapper mapper;

  public LoanBookInstanceAdapterRepository(BookInstanceJpaRepository jpaRepo, BookInstanceLoanMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    return jpaRepo.findById(bookInstanceId)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional
  public BookInstance save(BookInstance bookInstance) {
    BookInstanceEntity entity = jpaRepo.findById(bookInstance.getId()).orElse(null);

    if (entity != null) mapper.updateBookInstanceEntityFromBookInstance(bookInstance, entity);
    else entity = mapper.toEntity(bookInstance);

    return mapper.toDomain(jpaRepo.saveAndFlush(entity));
  }
}
