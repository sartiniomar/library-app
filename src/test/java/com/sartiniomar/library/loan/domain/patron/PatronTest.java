package com.sartiniomar.library.loan.domain.patron;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PatronTest {

  @Test
  void should_create_successfully_regular_patron() {
    UUID id = UUID.randomUUID();
    Patron patron = new Patron(id, PatronType.REGULAR);

    assertEquals(id, patron.getId());
    assertEquals(PatronType.REGULAR, patron.getType());
  }

  @Test
  void should_create_successfully_researcher_patron() {
    UUID id = UUID.randomUUID();
    Patron patron = new Patron(id, PatronType.RESEARCHER);

    assertEquals(id, patron.getId());
    assertTrue(patron.isResearcher());
    assertEquals(PatronType.RESEARCHER, patron.getType());
  }

  @Test
  void should_return_true_when_patron_is_researcher() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.RESEARCHER);

    assertTrue(patron.isResearcher());
  }

  @Test
  void should_return_false_when_patron_is_not_researcher() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);

    assertFalse(patron.isResearcher());
  }

  @Test
  void regular_patron_has_limit_of_3_loans() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);

    assertEquals(3, patron.maxLoans());
  }

  @Test
  void researcher_has_unlimited_loans() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.RESEARCHER);

    assertEquals(Integer.MAX_VALUE, patron.maxLoans());
  }
}