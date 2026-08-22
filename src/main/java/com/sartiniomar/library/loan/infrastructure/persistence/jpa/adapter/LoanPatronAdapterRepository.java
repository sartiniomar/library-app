package com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.infrastructure.mapper.PatronLoanMapper;
import com.sartiniomar.library.loan.infrastructure.mapper.PatronLoanMapperImpl;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronJpaRepository;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Import(PatronLoanMapperImpl.class)
public class LoanPatronAdapterRepository implements PatronLoanRepository {

  private final PatronJpaRepository jpaRepo;

  private final PatronLoanMapper mapper;

  public LoanPatronAdapterRepository(PatronJpaRepository jpaRepo, PatronLoanMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Optional<Patron> findById(UUID patronId) {
    return jpaRepo.findById(patronId)
        .map(mapper::toDomain);
  }
}
