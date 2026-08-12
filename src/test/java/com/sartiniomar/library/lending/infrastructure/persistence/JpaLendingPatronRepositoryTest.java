package com.sartiniomar.library.lending.infrastructure.persistence;

import com.sartiniomar.library.lending.domain.patron.PatronType;
import com.sartiniomar.library.lending.infrastructure.persistence.adapter.LendingPatronJpaRepository;
import com.sartiniomar.library.lending.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronSpringDataRepository;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.model.PatronEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(LendingPatronJpaRepository.class)
class JpaLendingPatronRepositoryTest {

  @Autowired
  private LendingPatronJpaRepository repository;

  @Autowired
  private PatronSpringDataRepository patronDataRepository;

  @Test
  void shouldSaveAndFindPatron() {
    UUID id = UUID.randomUUID();
    PatronEntity entity = new PatronEntity();
    entity.setId(id);
    entity.setType(com.sartiniomar.library.patron.domain.patron.PatronType.REGULAR);
    entity.setName("John Doe");
    entity.setEmail("john.doe@example.com");

    patronDataRepository.save(entity);

    Optional<Patron> result = repository.findById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
    assertEquals(PatronType.REGULAR, result.get().getType());
    assertFalse(result.get().isResearcher());
  }
}