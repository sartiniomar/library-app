package com.sartiniomar.library.patron.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.CreateRegularPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.CreateResearcherPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.DeletePatronUseCase;
import com.sartiniomar.library.patron.application.port.in.GetPatronByIdUseCase;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import com.sartiniomar.library.patron.support.builder.PatronTestDataBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PatronControllerTest extends LibraryApplicationTests {

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

  private static Stream<Arguments> provideDataForGroupBadRequest() {
    return Stream.of(
        Arguments.of("patron/createPatronWithNameBlankRequest.json", "name is required"),
        Arguments.of("patron/createPatronWithEmailBlankRequest.json", "email is required"),
        Arguments.of("patron/createPatronWithNameNullRequest.json", "name is required"),
        Arguments.of("patron/createPatronWithEmailNullRequest.json", "email is required")
    );
  }

  @Test
  @SneakyThrows
  void shouldCreateRegularPatronResponse() {
    ArgumentCaptor<CreatePatronCommand> patronCaptor = ArgumentCaptor.forClass(CreatePatronCommand.class);

    Patron patron = new PatronTestDataBuilder().buildDefaultRegular();

    when(createRegularPatron.execute(patronCaptor.capture())).thenReturn(patron);

    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.type").value("REGULAR"))
        .andExpect(jsonPath("$.email").value("name@email.com"));

    assertEquals("Name", patronCaptor.getValue().name());
    assertEquals("name@email.com", patronCaptor.getValue().email());

    verify(createRegularPatron, times(1)).execute(patronCaptor.getValue());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupBadRequest")
  @SneakyThrows
  void shouldReturnBadRequestForInvalidInputCreatingRegularPatron(String requestFilePath, String description) {
    String bodyRequest = getContentFromFile(requestFilePath);

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value(description));

    verify(createRegularPatron, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForDuplicateEmailCreateRegularPatron() {
    when(createRegularPatron.execute(any())).thenThrow(new PatronAlreadyExistsException("Email already exists"));

    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description").value("Email already exists"));

    verify(createRegularPatron, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldCreateResearcherPatronResponse() {
    ArgumentCaptor<CreatePatronCommand> patronCaptor = ArgumentCaptor.forClass(CreatePatronCommand.class);

    Patron patron = new PatronTestDataBuilder().buildDefaultResearcher();

    when(createResearcherPatron.execute(patronCaptor.capture())).thenReturn(patron);

    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.email").value("name@email.com"))
        .andExpect(jsonPath("$.type").value("RESEARCHER"));

    assertEquals("Name", patronCaptor.getValue().name());
    assertEquals("name@email.com", patronCaptor.getValue().email());

    verify(createResearcherPatron, times(1)).execute(patronCaptor.getValue());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupBadRequest")
  @SneakyThrows
  void shouldReturnBadRequestForInvalidInputCreatingResearcherPatron(String requestFilePath, String description) {
    String bodyRequest = getContentFromFile(requestFilePath);

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value(description));

    verify(createResearcherPatron, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForDuplicateEmailCreateResearcherPatron() {
    when(createResearcherPatron.execute(any())).thenThrow(new PatronAlreadyExistsException("Email already exists"));

    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description").value("Email already exists"));

    verify(createResearcherPatron, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnUpdateRegularPatronResponse() {
    ArgumentCaptor<UpdatePatronCommand> updatePatronCommandArgumentCaptor =
        ArgumentCaptor.forClass(UpdatePatronCommand.class);

    Patron patron = new PatronTestDataBuilder()
        .build(UUID.randomUUID(), PatronType.RESEARCHER, "Other name", "other@example.com");

    when(updatePatron.execute(updatePatronCommandArgumentCaptor.capture())).thenReturn(patron);

    String bodyRequest = getContentFromFile("patron/updatePatronRequest.json");

    mockMvc.perform(put("/patrons/{id}", patron.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Other name"))
        .andExpect(jsonPath("$.type").value("RESEARCHER"))
        .andExpect(jsonPath("$.email").value("other@example.com"));

    assertEquals(patron.getId(), updatePatronCommandArgumentCaptor.getValue().id());
    assertEquals(PatronType.RESEARCHER, updatePatronCommandArgumentCaptor.getValue().type());
    assertEquals("Other name", updatePatronCommandArgumentCaptor.getValue().name());
    assertEquals("other@example.com", updatePatronCommandArgumentCaptor.getValue().email());

    verify(updatePatron, times(1)).execute(updatePatronCommandArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForDuplicateEmailOnUpdate() {
    UUID id = UUID.randomUUID();
    when(updatePatron.execute(any())).thenThrow(new PatronAlreadyExistsException("Email already exists"));;

    String bodyRequest = getContentFromFile("patron/updatePatronRequest.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description").value("Email already exists"));

    verify(updatePatron, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnUpdate() {
    String bodyRequest = getContentFromFile("patron/updatePatronRequest.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/patrons/invalid-uuid")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(updatePatron, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingPatronOnUpdate() {
    UUID id = UUID.randomUUID();
    when(updatePatron.execute(any())).thenThrow(new PatronNotFoundException("Patron not found with id: " + id));

    String bodyRequest = getContentFromFile("patron/updatePatronRequest.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Patron not found with id: " + id));

    verify(updatePatron, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldGetById() {
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

    Patron patron = new PatronTestDataBuilder().buildDefaultRegular();

    when(getPatronById.execute(uuidArgumentCaptor.capture())).thenReturn(patron);

    mockMvc.perform(get("/patrons/" + patron.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(patron.getId().toString()))
        .andExpect(jsonPath("$.name").value("Name"))
        .andExpect(jsonPath("$.email").value("name@email.com"))
        .andExpect(jsonPath("$.type").value("REGULAR"));

    assertEquals(patron.getId(), uuidArgumentCaptor.getValue());

    verify(getPatronById, times(1)).execute(uuidArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingPatronOnGetById() {
    UUID id = UUID.randomUUID();

    when(getPatronById.execute(id)).thenThrow(new PatronNotFoundException("Patron not found with id: " + id));

    mockMvc.perform(get("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Patron not found with id: " + id));

    verify(getPatronById, times(1)).execute(id);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnGetById() {
    mockMvc.perform(get("/patrons/invalid-uuid")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(getPatronById, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldDeletePatron() {
    UUID id = UUID.randomUUID();

    mockMvc.perform(MockMvcRequestBuilders.delete("/patrons/{id}", id))
        .andExpect(status().isNoContent());

    verify(deletePatron, times(1)).execute(id);
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingPatronOnDelete() {
    UUID id = UUID.randomUUID();

    doThrow(new PatronNotFoundException("Patron not found with id: " + id)).when(deletePatron).execute(any());

    mockMvc.perform(MockMvcRequestBuilders.delete("/patrons/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Patron not found with id: " + id));

    verify(deletePatron, times(1)).execute(id);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnDelete() {
    mockMvc.perform(MockMvcRequestBuilders.delete("/patrons/invalid-uuid"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(deletePatron, never()).execute(any());
  }
}
