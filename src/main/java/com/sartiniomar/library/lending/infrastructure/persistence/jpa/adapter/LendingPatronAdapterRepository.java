package com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.lending.application.port.out.PatronLendingRepository;
import com.sartiniomar.library.lending.domain.patron.Patron;
import com.sartiniomar.library.lending.infrastructure.mapper.PatronHoldMapper;
import com.sartiniomar.library.lending.infrastructure.mapper.PatronHoldMapperImpl;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronJpaRepository;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Import(PatronHoldMapperImpl.class)
public class LendingPatronAdapterRepository implements PatronLendingRepository {

  private final PatronJpaRepository jpaRepo;

  private final PatronHoldMapper mapper;

  public LendingPatronAdapterRepository(PatronJpaRepository jpaRepo, PatronHoldMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Optional<Patron> findById(UUID patronId) {
    return jpaRepo.findById(patronId)
        .map(mapper::toDomain);
  }
}
