package com.sartiniomar.library.patron.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.patron.application.port.in.CreateRegularPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.CreateResearcherPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.DeletePatronUseCase;
import com.sartiniomar.library.patron.application.port.in.GetPatronByIdUseCase;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapper;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronSpringDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PatronControllerTest extends LibraryApplicationTests {

  @Autowired
  private PatronMapper mapper;

  @MockBean
  PatronSpringDataRepository patronSpringDataRepository;

  @MockBean
  private CreateRegularPatronUseCase createRegularPatron;

  @MockBean
  private CreateResearcherPatronUseCase createResearcherPatron;

  @MockBean
  private GetPatronByIdUseCase getPatronById;

  @MockBean
  private UpdatePatronUseCase updatePatron;

  @MockBean
  private DeletePatronUseCase deletePatron;

  @Test
  void shouldCreateRegularPatron() throws Exception {

    UUID id = UUID.randomUUID();

    Patron patron = new Patron(id, PatronType.REGULAR, "John Doe", "johnDoe@example.com");

    when(createRegularPatron.execute(any()))
        .thenReturn(patron);

    String json = """
        {
          "name": "John Doe",
          "email": "johnDoe@example.com"
        }
        """;

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.type").value("REGULAR"))
        .andExpect(jsonPath("$.email").value("johnDoe@example.com"));
  }

  @Test
  void shouldCreateResearcherPatron() throws Exception {

    UUID id = UUID.randomUUID();

    Patron patron = new Patron(id, PatronType.RESEARCHER, "John Doe", "johnDoe@example.com");

    when(createResearcherPatron.execute(any()))
        .thenReturn(patron);

    String json = """
        {
          "name": "John Doe",
          "email": "johnDoe@example.com"
        }
        """;

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.email").value("johnDoe@example.com"))
        .andExpect(jsonPath("$.type").value("RESEARCHER"));
  }

  @Test
  void shouldReturnBadRequestForInvalidInputCreateRegularPatron() throws Exception {
    String json = """
        {
          "name": "",
          "email": "johnDoe@example.com"
        }
        """;

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForInvalidInputCreateResearcherPatron() throws Exception {
    String json = """
        {
          "name": "John Doe",
          "email": "",
        }
        """;

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForMissingFieldsCreateRegularPatron() throws Exception {
    String json = """
        {
          "email": "johnDoe@example.com"
        }
        """;

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForMissingFieldsCreateResearcherPatron() throws Exception {
    String json = """
        {
          "name": "John Doe"
        }
        """;

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForNullFieldsCreateRegularPatron() throws Exception {
    String json = """
        {
          "name": null,
          "email": "johnDoe@example.com"
        }
        """;

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test   void shouldReturnBadRequestForNullFieldsCreateResearcherPatron() throws Exception {
    String json = """
        {
          "name": "John Doe",
          "email": null

        }
        """;

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForDuplicateEmailCreateRegularPatron() throws Exception {
    when(createRegularPatron.execute(any()))
        .thenThrow(new PatronAlreadyExistsException("Email already exists"));

    String json = """
        {
          "name": "John Doe",
          "email": "johnDoe@example.com"
        }
        """;

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void shouldReturnBadRequestForDuplicateEmailCreateResearcherPatron() throws Exception {
    when(createResearcherPatron.execute(any()))
        .thenThrow(new PatronAlreadyExistsException("Email already exists"));

    String json = """
        {
          "name": "John Doe",
          "email": "johnDoe@example.com"
        }
        """;

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void shouldUpdatePatron() throws Exception {
    UUID id = UUID.randomUUID();
    Patron patron = new Patron(id, PatronType.REGULAR, "John Doe", "johnDoe@example.com");
    Patron patronUpdated = new Patron(id, PatronType.RESEARCHER, "John Doe", "johnDoe2@example.com");

    patronSpringDataRepository.save(mapper.toEntity(patron));

    when(updatePatron.execute(any()))
        .thenReturn(patronUpdated);

    String json = """
        {
          "type": "RESEARCHER",
          "email": "johnDoe2@example.com"
        }
        """;

    mockMvc.perform(put("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.type").value("RESEARCHER"))
        .andExpect(jsonPath("$.email").value("johnDoe2@example.com"));
  }

  @Test
  void shouldReturnBadRequestForDuplicateEmailOnUpdate() throws Exception {
    UUID id = UUID.randomUUID();

    when(updatePatron.execute(any()))
        .thenThrow(new PatronAlreadyExistsException("Email already exists"));;

    String json = """
        {
          "name": "Other Title",
          "email": "other@example.com"
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/patrons/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void shouldReturnBadRequestForInvalidUuidOnUpdate() throws Exception {
    String invalidId = "invalid-uuid";

    String json = """
        {
          "name": "Other Title",
          "email": "other@example.com"
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/patrons/" + invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundForNonExistingPatronOnUpdate() throws Exception {
    UUID id = UUID.randomUUID();

    when(updatePatron.execute(any()))
        .thenThrow(new PatronNotFoundException("Patron not found"));

    String json = """
        {
          "name": "Other Name",
          "email": "other@example.com"
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/patrons/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldGetById() throws Exception {
    UUID id = UUID.randomUUID();
    Patron patron = new Patron(id, PatronType.REGULAR, "Name", "name@email.com");

    when(getPatronById.execute(any()))
        .thenReturn(patron);

    mockMvc.perform(get("/patrons/" + id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.email").value("name@email.com"))
        .andExpect(jsonPath("$.type").value("REGULAR"));
  }

  @Test
  void shouldReturnNotFoundForNonExistingPatronOnGetById() throws Exception {
    UUID inexistentId = UUID.randomUUID();

    when(getPatronById.execute(inexistentId))
        .thenThrow(new PatronNotFoundException("Patron not found"));

    mockMvc.perform(get("/patrons/" + inexistentId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForInvalidUuidOnGetById() throws Exception {
    String invalidId = "invalid-uuid";

    mockMvc.perform(get("/patrons/" + invalidId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldDeletePatron() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc.perform(MockMvcRequestBuilders.delete("/patrons/" + id))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundForNonExistingPatronOnDelete() throws Exception {
    UUID id = UUID.randomUUID();

    doThrow(new PatronNotFoundException("Patron not found"))
        .when(deletePatron).execute(any());

    mockMvc.perform(MockMvcRequestBuilders.delete("/patrons/" + id))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForInvalidUuidOnDelete() throws Exception {
    String invalidId = "invalid-uuid";

    mockMvc.perform(MockMvcRequestBuilders.delete("/patrons/" + invalidId))
        .andExpect(status().isBadRequest());
  }
}
