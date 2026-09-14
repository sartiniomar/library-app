package com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapperImpl;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapper;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.repository.LoanJpaRepository;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class LoanAdapterRepository implements LoanRepository {

  private final LoanJpaRepository repository;
  private final PatronJpaRepository patronJpaRepository;
  private final BookInstanceJpaRepository bookInstanceJpaRepository;
  private final LoanMapper mapper;

  public LoanAdapterRepository(LoanJpaRepository repository, PatronJpaRepository patronJpaRepository, BookInstanceJpaRepository bookInstanceJpaRepository) {
    this.repository = repository;
    this.patronJpaRepository = patronJpaRepository;
    this.bookInstanceJpaRepository = bookInstanceJpaRepository;
    this.mapper = new LoanMapperImpl();
  }

  @Override
  public Long countActiveLoansByPatronId(UUID patronId, List<LoanStatus> statuses) {
    return repository.countActiveLoansByPatronId(patronId, statuses);
  }

  @Override
  @Transactional
  public Loan save(Loan loan) {

    LoanEntity entity = mapper.toEntity(loan);

    entity.setPatron(
        patronJpaRepository.getReferenceById(loan.getPatronId())
    );

    entity.setBookInstance(
        bookInstanceJpaRepository.getReferenceById(loan.getBookInstanceId())
    );

    return mapper.toDomain(repository.saveAndFlush(entity));
  }

  @Override
  public Optional<Loan> findById(UUID id) {
    Optional<LoanEntity> entityOpt = repository.findById(id);
    return entityOpt.map(mapper::toDomain);
  }

  @Override
  public List<Loan> findAllByPatronId(UUID patronId) {
    return repository.findAllByPatron_Id(patronId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }
}