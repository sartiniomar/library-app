package com.sartiniomar.library.lending.infrastructure.persistence.jpa;

import com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter.LendingPatronJpaRepository;
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

  // Repositorio JPA del módulo "patron" para preparar datos en la BD de test
  @Autowired
  private PatronSpringDataRepository patronDataRepository;

  @Test
  void shouldSaveAndFindPatron() {
    // Preparar entidad JPA y guardarla directamente en la BD de pruebas
    UUID id = UUID.randomUUID();
    PatronEntity entity = new PatronEntity();
    entity.setId(id);
    entity.setType("REGULAR");          // tal como espera tu mapper: "RESEARCHER" o "REGULAR"
    entity.setName("John Doe");
    entity.setEmail("john.doe@example.com");

    patronDataRepository.save(entity);

    // Ahora usar el adaptador lending para recuperar el domain Patron
    Optional<Patron> result = repository.findById(id);

    assertTrue(result.isPresent());
    // (opcional) validar que el domain Patron es el tipo esperado
    // assertFalse(result.get().isResearcher());
  }
}