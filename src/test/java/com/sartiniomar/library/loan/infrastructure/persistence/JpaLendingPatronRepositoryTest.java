package com.sartiniomar.library.loan.infrastructure.persistence;

import com.sartiniomar.library.loan.domain.patron.PatronType;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanPatronAdapterRepository;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronJpaRepository;
import com.sartiniomar.library.patron.infrastructure.persistence.model.PatronEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(LoanPatronAdapterRepository.class)
class JpaLendingPatronRepositoryTest {

  @Autowired
  private LoanPatronAdapterRepository repository;

  @Autowired
  private PatronJpaRepository patronDataRepository;

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