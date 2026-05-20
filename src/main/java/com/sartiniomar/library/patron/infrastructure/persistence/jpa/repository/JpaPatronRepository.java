package com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.adapter.PatronSpringDataRepository;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.mapper.PatronMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaPatronRepository implements PatronRepository {

  private final PatronSpringDataRepository jpaRepo;

  public JpaPatronRepository(PatronSpringDataRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public Patron save(Patron patron) {
    return PatronMapper.toDomain(
        jpaRepo.save(PatronMapper.toEntity(patron))
    );
  }

  @Override
  public Optional<Patron> findById(UUID id) {
    return jpaRepo.findById(id)
        .map(PatronMapper::toDomain);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepo.existsByEmail(email);
  }

  @Override
  public void delete(UUID id) {
    jpaRepo.deleteById(id);
  }

  @Override
  public Optional<Patron> findByEmail(String email) {
    return jpaRepo.findByEmail(email)
        .map(PatronMapper::toDomain);
  }
}
