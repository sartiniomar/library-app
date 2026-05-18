package com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.lending.application.port.out.PatronRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.mapper.PatronMapper;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository.SpringDataPatronRepository;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


@Repository
public class JpaPatronRepository implements PatronRepository {

  private final SpringDataPatronRepository jpaRepo;

  public JpaPatronRepository(SpringDataPatronRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public Optional<Patron> findById(UUID patronId) {
    return jpaRepo.findById(patronId)
        .map(PatronMapper::toDomain);
  }

  @Override
  public void save(Patron patron) {
    jpaRepo.save(PatronMapper.toEntity(patron));
  }
}
