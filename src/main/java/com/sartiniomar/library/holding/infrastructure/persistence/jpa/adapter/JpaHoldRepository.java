package com.sartiniomar.library.holding.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.holding.application.port.out.HoldRepository;
import com.sartiniomar.library.holding.infrastructure.persistence.jpa.mapper.HoldMapper;
import com.sartiniomar.library.holding.infrastructure.persistence.jpa.repository.SpringDataHoldRepository;
import com.sartiniomar.library.holding.model.hold.Hold;
import org.springframework.stereotype.Repository;

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
}