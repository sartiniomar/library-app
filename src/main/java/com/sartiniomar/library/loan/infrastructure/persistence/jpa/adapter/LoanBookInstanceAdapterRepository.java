package com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookJpaRepository;
import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.infrastructure.mapper.BookInstanceLoanMapper;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.loan.infrastructure.mapper.BookInstanceLoanMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
@Import(BookInstanceLoanMapperImpl.class)
public class LoanBookInstanceAdapterRepository implements BookInstanceLoanRepository {

  private final BookInstanceJpaRepository bookInstanceJpaRepo;
  private final BookJpaRepository bookJpaRepo;
  private final BookInstanceLoanMapper mapper;

  public LoanBookInstanceAdapterRepository(BookInstanceJpaRepository bookInstanceJpaRepo, BookJpaRepository bookJpaRepo, BookInstanceLoanMapper mapper) {
    this.bookInstanceJpaRepo = bookInstanceJpaRepo;
    this.bookJpaRepo = bookJpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    log.debug("Finding book instance. Book Instance Id= {}", bookInstanceId);
    return bookInstanceJpaRepo.findById(bookInstanceId)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional
  public BookInstance save(BookInstance bookInstance) {
    BookInstanceEntity bookInstanceEntity;
    Optional<BookInstanceEntity> entityOptional = bookInstanceJpaRepo.findById(bookInstance.getId());
    if (entityOptional.isPresent()) {
      log.debug("Updating book instance. Book Instance Id= {}", bookInstance.getId());
      bookInstanceEntity = entityOptional.get();
      mapper.updateBookInstanceEntityFromBookInstance(bookInstance, bookInstanceEntity);
    } else {
      log.debug("Saving book instance. Book Instance Id= {}", bookInstance.getId());
      bookInstanceEntity = mapper.toEntity(bookInstance);
      bookInstanceEntity.setBook(
          bookJpaRepo.getReferenceById(bookInstance.getBookId())
      );
    }
    return mapper.toDomain(bookInstanceJpaRepo.saveAndFlush(bookInstanceEntity));
  }
}
