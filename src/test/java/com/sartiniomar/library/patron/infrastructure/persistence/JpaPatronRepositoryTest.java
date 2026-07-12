package com.sartiniomar.library.patron.infrastructure.persistence;

import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapper;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapperImpl;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronSpringDataRepository;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.adapter.PatronJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(PatronMapperImpl.class)
public class JpaPatronRepositoryTest {

  @Autowired
  private PatronSpringDataRepository jpaRepo;

  @Autowired
  private PatronMapper mapper;

  private PatronJpaRepository repository;

  @BeforeEach
  void setup() {
    repository = new PatronJpaRepository(jpaRepo, mapper);
  }

  @Test
  void shouldSaveAndFindPatronById() {

    Patron patron = Patron.regular(
        "John Doe",
        "john.doe@example.com"
    );

    repository.save(patron);

    Optional<Patron> foundPatron = repository.findById(patron.getId());

    assertTrue(foundPatron.isPresent());
    assertThat(foundPatron).isNotNull();
    assertThat(foundPatron.get().getName()).isEqualTo("John Doe");
  }

  @Test
  void shouldDeletePatron() {
    Patron patron = Patron.regular(
        "John Doe",
        "john.doe@example.com"
    );

    Patron saved = repository.save(patron);

    repository.delete(patron.getId());

    Optional<Patron> foundPatron = repository.findById(patron.getId());
    assertTrue(foundPatron.isEmpty());
  }

  @Test
  void shouldExistByEmail() {
    Patron patron = Patron.regular(
        "John Doe",
        "john.doe@example.com"
    );

    repository.save(patron);

    assertTrue(repository.existsByEmail(patron.getEmail()));
  }

  @Test
  void shoulFindByEmail() {
    Patron patron = Patron.regular(
        "John Doe",
        "john.doe@example.com"
    );

    repository.save(patron);

    Optional<Patron> foundPatron = repository.findByEmail(patron.getEmail());
    assertTrue(foundPatron.isPresent());
  }
}
