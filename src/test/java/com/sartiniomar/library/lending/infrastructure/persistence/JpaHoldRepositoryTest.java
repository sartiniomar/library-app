package com.sartiniomar.library.lending.infrastructure.persistence;

import com.sartiniomar.library.lending.domain.hold.Hold;
import com.sartiniomar.library.lending.infrastructure.persistence.adapter.HoldJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(HoldJpaRepository.class)
public class JpaHoldRepositoryTest {

  @Autowired
  private HoldJpaRepository repository;

  @Test
  void shouldSaveAndFindHold() {
    UUID bookInstanceId = java.util.UUID.randomUUID();
    UUID patronId = java.util.UUID.randomUUID();

    Hold hold = new Hold(patronId, bookInstanceId);

    repository.save(hold);

    Optional<Hold> result = repository.findById(hold.getId());

    assertTrue(result.isPresent());
    assertEquals(patronId, result.get().getPatronId());
    assertEquals(bookInstanceId, result.get().getBookInstanceId());
  }

  @Test
  void shouldCountHoldsByPatronId() {
    UUID patronId = java.util.UUID.randomUUID();

    Hold hold1 = new Hold(patronId, java.util.UUID.randomUUID());
    Hold hold2 = new Hold(patronId, java.util.UUID.randomUUID());

    repository.save(hold1);
    repository.save(hold2);

    int count = repository.countByPatronId(patronId);

    assertEquals(2, count);
  }
}