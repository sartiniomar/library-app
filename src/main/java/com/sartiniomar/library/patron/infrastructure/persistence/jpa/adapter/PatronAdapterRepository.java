package com.sartiniomar.library.patron.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapperImpl;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapper;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
@Import(PatronMapperImpl.class)
public class PatronAdapterRepository implements PatronRepository {

  private final PatronJpaRepository jpaRepo;

  private final PatronMapper mapper;

  public PatronAdapterRepository(PatronJpaRepository jpaRepo, PatronMapper mapper) {
    this.jpaRepo = jpaRepo;
    this.mapper = mapper;
  }

  @Override
  public Patron save(Patron patron) {
    log.debug("Saving patron. Patron Id= {}", patron.getId());
    return mapper.toDomain(jpaRepo.save(mapper.toEntity(patron)));
  }

  @Override
  public Optional<Patron> findById(UUID id) {
    log.debug("Finding patron. Patron Id= {}", id);
    return jpaRepo.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public boolean existsByEmail(String email) {
    log.debug("Existing patron. Email= {}", email);
    return jpaRepo.existsByEmail(email);
  }

  @Override
  public void delete(UUID id) {
    log.debug("Deleting patron. Patron Id= {}", id);
    jpaRepo.deleteById(id);
  }

  @Override
  public Optional<Patron> findByEmail(String email) {
    log.debug("Finding patron. Email= {}", email);
    return jpaRepo.findByEmail(email).map(mapper::toDomain);
  }
}
