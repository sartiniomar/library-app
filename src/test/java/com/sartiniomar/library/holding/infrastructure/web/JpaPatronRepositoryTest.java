package com.sartiniomar.library.holding.infrastructure.web;

import com.sartiniomar.library.holding.infrastructure.persistence.jpa.adapter.JpaPatronRepository;
import com.sartiniomar.library.holding.model.patron.Patron;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaPatronRepository.class)
class JpaPatronRepositoryTest {

  @Autowired
  private JpaPatronRepository repository;

  @Test
  void shouldSaveAndFindPatron() {
    Patron patron = Patron.regular();

    repository.save(patron);

    Optional<Patron> result = repository.findById(patron.getId());

    assertTrue(result.isPresent());
  }
}