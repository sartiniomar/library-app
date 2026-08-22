package com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapperImpl;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapper;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.repository.LoanJpaRepository;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Import(LoanMapperImpl.class)
public class LoanAdapterRepository implements LoanRepository {

  private final LoanJpaRepository jpaRepo;

  private final LoanMapper loanMapper;

  public LoanAdapterRepository(LoanJpaRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
    this.loanMapper = new LoanMapperImpl();
  }

  @Override
  public Integer countByPatronId(UUID patronId) {
    return (int) jpaRepo.countByPatronId(patronId);
  }

  @Override
  public void save(Loan hold) {
    jpaRepo.save(loanMapper.toEntity(hold));
  }

  @Override
  public Optional<Loan> findById(UUID id) {
    Optional<LoanEntity> entityOpt = jpaRepo.findById(id);
    return entityOpt.map(loanMapper::toDomain);
  }
}