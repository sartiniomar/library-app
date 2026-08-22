package com.sartiniomar.library.patron.domain.patron;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatronTest {

  @Test
  void shouldCreateRegularPatronWithValidData() {
    Patron patron = Patron.regular("John Doe", "john.doe@example.com");

    assert patron.getId() != null;
    assert patron.getType() == PatronType.REGULAR;
    assert patron.getName().equals("John Doe");
    assert patron.getEmail().equals("john.doe@example.com");
  }

  @Test
  void shouldCreateResearcherPatronWithValidData() {
    Patron patron = Patron.researcher("Jane Smith", "jane.smith@example.com");

    assert patron.getId() != null;
    assert patron.getType() == PatronType.RESEARCHER;
    assert patron.getName().equals("Jane Smith");
    assert patron.getEmail().equals("jane.smith@example.com");
  }

  @Test
  void shouldCreatePatronWithGivenId() {
    Patron patron =
        new Patron(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            PatronType.REGULAR,
            "John Doe",
            "john.doe@example.com");

    assert patron.getId().equals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
    assert patron.getType() == PatronType.REGULAR;
    assert patron.getName().equals("John Doe");
    assert patron.getEmail().equals("john.doe@example.com");
  }

  @Test
  void shouldNotAllowCreatePatronWithEmptyName() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.regular("", "john.doe@example.com")
    );
  }

  @Test
  void shouldNotAllowCreatePatronWithEmptyEmail() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.regular("John Doe", "")
    );
  }

  @Test
  void shouldNotAllowCreateRegularPatronWithNullName() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.regular(null, "john.doe@example.com")
    );
  }

  @Test
  void shouldNotAllowCreateRegularPatronWithNullEmail() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.regular("John Doe", null)
    );
  }

  @Test
  void shouldNotAllowCreateResearcherPatronWithEmptyName() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.researcher("", "jane.smith@example.com")
    );
  }

  @Test
  void shouldNotAllowCreateResearcherPatronWithEmptyEmail() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.researcher("Jane Smith", "")
    );
  }

  @Test
  void shouldNotAllowCreateResearcherPatronWithNullName() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.researcher(null, "jane.smith@example.com")
    );
  }

  @Test
  void shouldNotAllowCreateResearcherPatronWithNullEmail() {
    assertThrows(IllegalArgumentException.class, () ->
        Patron.researcher("Jane Smith", null)
    );
  }

  @Test
  void shouldUpdateOnlyProvidedFields() {
    Patron patron = Patron.regular("John Doe", "john.doe@example.com");

    patron.update(PatronType.RESEARCHER, "Jane Smith", null);

    assert patron.getType() == PatronType.RESEARCHER;
    assert patron.getName().equals("Jane Smith");
    assert patron.getEmail().equals("john.doe@example.com");
  }
}