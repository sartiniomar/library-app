package com.sartiniomar.library.patron.infrastructure.persistence;

import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapper;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapperImpl;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronJpaRepository;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.adapter.PatronAdapterRepository;
import com.sartiniomar.library.patron.support.builder.PatronTestDataBuilder;
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
  private PatronJpaRepository jpaRepo;

  @Autowired
  private PatronMapper mapper;

  private PatronAdapterRepository repository;

  @BeforeEach
  void setup() {
    repository = new PatronAdapterRepository(jpaRepo, mapper);
  }

  @Test
  void shouldSaveAndFindPatronById() {

    Patron patron = new PatronTestDataBuilder().buildDefaultRegular();

    repository.save(patron);

    Optional<Patron> foundPatron = repository.findById(patron.getId());

    assertTrue(foundPatron.isPresent());
    assertThat(foundPatron).isNotNull();
    assertThat(foundPatron.get().getName()).isEqualTo("Name");
  }

  @Test
  void shouldDeletePatron() {
    Patron patron = new PatronTestDataBuilder().buildDefaultRegular();

    Patron saved = repository.save(patron);

    repository.delete(patron.getId());

    Optional<Patron> foundPatron = repository.findById(patron.getId());
    assertTrue(foundPatron.isEmpty());
  }

  @Test
  void shouldExistByEmail() {
    Patron patron = new PatronTestDataBuilder().buildDefaultRegular();

    repository.save(patron);

    assertTrue(repository.existsByEmail(patron.getEmail()));
  }

  @Test
  void shouldFindByEmail() {
    Patron patron = new PatronTestDataBuilder().buildDefaultRegular();

    repository.save(patron);

    Optional<Patron> foundPatron = repository.findByEmail(patron.getEmail());
    assertTrue(foundPatron.isPresent());
  }
}
