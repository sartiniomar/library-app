package com.sartiniomar.library.lending.domain.patron;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatronTest {

  @Test
  void should_create_successfuly_regular_patron() {
    Patron patron = Patron.regular();

    assertNotNull(patron.getId());
    assertEquals(PatronType.REGULAR, patron.getType());
  }

  @Test
  void should_create_successfuly_researcher_patron() {
    Patron patron = Patron.researcher();

    assertNotNull(patron.getId());
    assertTrue(patron.isResearcher());
    assertEquals(PatronType.RESEARCHER, patron.getType());
  }

  @Test
  void regular_patron_has_limit_of_5_holds() {
    Patron patron = Patron.regular();

    assertEquals(5, patron.maxHolds());
  }

  @Test
  void researcher_has_unlimited_holds() {
    Patron patron = Patron.researcher();

    assertEquals(Integer.MAX_VALUE, patron.maxHolds());
  }
}