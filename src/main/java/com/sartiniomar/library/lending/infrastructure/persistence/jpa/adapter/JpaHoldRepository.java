package com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.lending.application.port.out.HoldRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.mapper.HoldMapper;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.HoldEntity;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository.SpringDataHoldRepository;
import com.sartiniomar.library.lending.domain.hold.Hold;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaHoldRepository implements HoldRepository {

  private final SpringDataHoldRepository jpaRepo;

  public JpaHoldRepository(SpringDataHoldRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public Integer countByPatronId(UUID patronId) {
    return (int) jpaRepo.countByPatronId(patronId);
  }

  @Override
  public void save(Hold hold) {
    jpaRepo.save(HoldMapper.toEntity(hold));
  }

  @Override
  public Optional<Hold> findById(UUID id) {
    Optional<HoldEntity> entityOpt = jpaRepo.findById(id);
    return entityOpt.map(HoldMapper::toDomain);
  }
}