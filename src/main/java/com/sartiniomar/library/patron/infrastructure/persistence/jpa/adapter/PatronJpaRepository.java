package com.sartiniomar.library.patron.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapperImpl;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapper;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronSpringDataRepository;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Import(PatronMapperImpl.class)
public class PatronJpaRepository implements PatronRepository {

  private final PatronSpringDataRepository jpaRepo;

  private final PatronMapper mapper;

  public PatronJpaRepository(PatronSpringDataRepository jpaRepo, PatronMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Patron save(Patron patron) {
    return mapper.toDomain(
        jpaRepo.save(mapper.toEntity(patron))
    );
  }

  @Override
  public Optional<Patron> findById(UUID id) {
    return jpaRepo.findById(id)
        .map(mapper::toDomain);
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
        .map(mapper::toDomain);
  }
}
