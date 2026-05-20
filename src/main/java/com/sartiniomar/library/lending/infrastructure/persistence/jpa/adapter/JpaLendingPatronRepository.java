package com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.lending.application.port.out.PatronLendingRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.mapper.PatronMapper;
import com.sartiniomar.library.lending.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.adapter.PatronSpringDataRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


@Repository
public class JpaLendingPatronRepository implements PatronLendingRepository {

  private final PatronSpringDataRepository jpaRepo;

  public JpaLendingPatronRepository(PatronSpringDataRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public Optional<Patron> findById(UUID patronId) {
    return jpaRepo.findById(patronId)
        .map(PatronMapper::toDomain);
  }
}
