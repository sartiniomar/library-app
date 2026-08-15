package com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.lending.application.port.out.HoldRepository;
import com.sartiniomar.library.lending.infrastructure.mapper.HoldMapperImpl;
import com.sartiniomar.library.lending.infrastructure.mapper.HoldMapper;
import com.sartiniomar.library.lending.infrastructure.persistence.model.HoldEntity;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository.HoldJpaRepository;
import com.sartiniomar.library.lending.domain.hold.Hold;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@Import(HoldMapperImpl.class)
public class HoldAdapterRepository implements HoldRepository {

  private final HoldJpaRepository jpaRepo;

  private HoldMapper holdMapper;

  public HoldAdapterRepository(HoldJpaRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
    this.holdMapper = new HoldMapperImpl();
  }

  @Override
  public Integer countByPatronId(UUID patronId) {
    return (int) jpaRepo.countByPatronId(patronId);
  }

  @Override
  public void save(Hold hold) {
    jpaRepo.save(holdMapper.toEntity(hold));
  }

  @Override
  public Optional<Hold> findById(UUID id) {
    Optional<HoldEntity> entityOpt = jpaRepo.findById(id);
    return entityOpt.map(holdMapper::toDomain);
  }
}