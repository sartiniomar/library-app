package com.sartiniomar.library.patron.infrastructure.web.integration;

import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import com.sartiniomar.library.patron.infrastructure.web.dto.PatronResponse;
import com.sartiniomar.library.patron.infrastructure.web.integration.support.factory.PatronTestFactory;
import com.sartiniomar.library.patron.infrastructure.web.integration.support.helper.PatronHttpHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.jdbc.JdbcTestUtils;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatronIntegrationTest extends PatronHttpHelper {

  @Autowired
  private PatronRepository patronRepository;

  @Autowired
  private PatronTestFactory patronFactory;

  @Test
  void shouldCreateRegularPatron() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron");

    PatronResponse response = createRegularPatron();
    flushAndClear();

    Patron savedPatron = patronRepository.findById(response.id()).orElseThrow();

    assertThat(savedPatron)
        .extracting("name", "email", "type")
        .containsExactly(response.name(), response.email(), response.type());

    assertEquals(PatronType.REGULAR, response.type());
    assertEquals(initialCount + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron"));
  }

  @Test
  void shouldReturnBadRequestForDuplicateEmailCreateRegularPatron() throws Exception {
    Patron patron = patronFactory.createDefaultRegular();
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron");

    ErrorResponse response = createRegularPatronDuplicateEmail();

    assertEquals("409 CONFLICT", response.code());
    assertEquals("Email " + patron.getEmail() + " already exists", response.errors().getFirst().description());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron"));
  }

  @Test
  void shouldCreateResearcherPatron() throws Exception {
    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron");

    PatronResponse response = createResearcherPatron();
    flushAndClear();

    Patron savedPatron = patronRepository.findById(response.id()).orElseThrow();

    assertThat(savedPatron)
        .extracting("name", "email", "type")
        .containsExactly(response.name(), response.email(), response.type());

    assertEquals(PatronType.RESEARCHER, response.type());
    assertEquals(initialCount + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron"));
  }

  @Test
  void shouldReturnBadRequestForDuplicateEmailCreateResearcherPatron() throws Exception {
    Patron patron = patronFactory.createDefaultResearcher();
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron");

    ErrorResponse response = createResearcherPatronDuplicateEmail();

    assertEquals("409 CONFLICT", response.code());
    assertEquals("Email " + patron.getEmail() + " already exists", response.errors().getFirst().description());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron"));
  }

  @Test
  void shouldUpdatePatron() throws Exception {
    Patron patron = patronFactory.createDefaultRegular();
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron");

    PatronResponse response = updatePatron(patron.getId().toString());
    flushAndClear();

    Patron patronUpdated = patronRepository.findById(response.id()).orElseThrow();

    assertThat(patronUpdated)
        .extracting("name", "email", "type")
        .containsExactly("Other name", "other@example.com", PatronType.RESEARCHER);

    assertEquals(patronUpdated.getId(), response.id());
    assertEquals(patronUpdated.getName(), response.name());
    assertEquals(patronUpdated.getEmail(), response.email());
    assertEquals(patronUpdated.getType(), response.type());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron"));
  }

  @Test
  void shouldReturnBadRequestForDuplicateEmailInUpdatePatron() throws Exception {
    Patron patron = patronFactory.createDefaultResearcher();
    Patron otherPatron = patronFactory.create("Other name", "other@example.com",  PatronType.REGULAR);
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron");

    ErrorResponse response = updatePatronDuplicateEmail(patron.getId().toString());

    assertEquals("409 CONFLICT", response.code());
    assertEquals("Email " + otherPatron.getEmail() + " already exists", response.errors().getFirst().description());

    assertEquals(initialCount, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron"));
  }

  @Test
  void shouldReturnNotFoundForNonExistingPatronOnUpdate() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = updatePatronNotFound(id);
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Patron not found with id: " + id, response.errors().getFirst().description());
  }

  @Test
  void shouldGetById() throws Exception {
    PatronResponse patron = createRegularPatron();
    flushAndClear();

    PatronResponse response = getById(patron.id().toString());

    assertEquals(patron.id(), response.id());
    assertEquals(patron.name(), response.name());
    assertEquals(patron.email(), response.email());
    assertEquals(patron.type(), response.type());
  }

  @Test
  void shouldReturnNotFoundForNonExistingPatronOnGetById() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = updatePatronNotFound(id);
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Patron not found with id: " + id, response.errors().getFirst().description());
  }

  @Test
  void shouldDeletePatron() throws Exception {
    PatronResponse response = createRegularPatron();
    flushAndClear();

    int initialCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron");

    deletePatron(response.id().toString());
    flushAndClear();

    assertEquals(initialCount - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patron"));
  }

  @Test
  void shouldReturnNotFoundForNonExistingPatronOnDelete() throws Exception {
    UUID id = UUID.randomUUID();
    ErrorResponse response = deletePatronNotFound(id.toString());
    assertEquals("404 NOT_FOUND", response.code());
    assertEquals("Patron not found with id: " + id, response.errors().getFirst().description());
  }
}
