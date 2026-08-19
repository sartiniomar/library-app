package com.sartiniomar.library.patron.infrastructure.web.integration.support.helper;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.patron.infrastructure.web.dto.PatronResponse;
import org.springframework.http.MediaType;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PatronHttpHelper extends LibraryApplicationTests {

  public PatronResponse createRegularPatron() throws Exception {
    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    String response = mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, PatronResponse.class);
  }

  public ErrorResponse createRegularPatronDuplicateEmail() throws Exception {
    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    String response = mockMvc.perform(post("/patrons/regular")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public PatronResponse createResearcherPatron() throws Exception {
    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    String response = mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, PatronResponse.class);
  }

  public ErrorResponse createResearcherPatronDuplicateEmail() throws Exception {
    String bodyRequest = getContentFromFile("patron/createPatronRequest.json");

    String response = mockMvc.perform(post("/patrons/researcher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public PatronResponse updatePatron(String id) throws Exception {
    String bodyRequest = getContentFromFile("patron/updatePatronRequest.json");

    String response = mockMvc.perform(put("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, PatronResponse.class);
  }

  public ErrorResponse updatePatronDuplicateEmail(String id) throws Exception {
    String bodyRequest = getContentFromFile("patron/updatePatronRequest.json");

    String response = mockMvc.perform(put("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public ErrorResponse updatePatronNotFound(UUID id) throws Exception {
    String bodyRequest = getContentFromFile("patron/updatePatronRequest.json");

    String response = mockMvc.perform(put("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public PatronResponse getById(String id) throws Exception {
    String response = mockMvc.perform(get("/patrons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, PatronResponse.class);
  }

  public void deletePatron(String id) throws Exception {
    mockMvc.perform(delete("/patrons/{id}", id))
        .andExpect(status().isNoContent());
  }

  public ErrorResponse deletePatronNotFound(String id) throws Exception {
    String response = mockMvc.perform(delete("/patrons/{id}", id))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }
}
