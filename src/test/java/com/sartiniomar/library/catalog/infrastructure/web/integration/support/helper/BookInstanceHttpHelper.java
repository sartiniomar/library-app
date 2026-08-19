package com.sartiniomar.library.catalog.infrastructure.web.integration.support.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookInstanceResponse;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookInstanceHttpHelper extends LibraryApplicationTests {

  public BookInstanceResponse createBookInstanceCirculating(UUID bookId) throws Exception {
    String response = mockMvc.perform(post("/books/{bookId}/instances/circulating", bookId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookInstanceResponse.class);
  }

  public ErrorResponse createBookInstanceCirculatingNotFound(UUID bookId) throws Exception {
    String response = mockMvc.perform(post("/books/{bookId}/instances/circulating", bookId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public BookInstanceResponse createBookInstanceRestricted(UUID bookId) throws Exception {
    String response = mockMvc.perform(post("/books/{bookId}/instances/restricted", bookId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookInstanceResponse.class);
  }

  public ErrorResponse createBookInstanceRestrictedNotFound(UUID bookId) throws Exception {
    String response = mockMvc.perform(post("/books/{bookId}/instances/restricted", bookId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public BookInstanceResponse updateBookInstanceCirculating(UUID id, UUID bookId) throws Exception {
    String bodyRequest = getContentFromFile("catalog/bookInstance/updateBookInstanceRequest.json");

    String response = mockMvc.perform(put("/books/{bookId}/instances/{id}", bookId, id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookInstanceResponse.class);
  }

  public ErrorResponse updateBookInstanceCirculatingNotFound(UUID id, UUID bookId) throws Exception {
    String bodyRequest = getContentFromFile("catalog/bookInstance/updateBookInstanceRequest.json");

    String response = mockMvc.perform(put("/books/{bookId}/instances/{id}", bookId, id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public BookInstanceResponse getBookInstanceById(UUID id) throws Exception {
    String response = mockMvc.perform(get("/books/{bookId}/instances/{id}", UUID.randomUUID(), id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookInstanceResponse.class);
  }

  public ErrorResponse getBookInstanceByIdNotFound(UUID id) throws Exception {
    String response = mockMvc.perform(get("/books/{bookId}/instances/{id}", UUID.randomUUID(), id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public List<BookInstanceResponse> getAllBookInstancesByBookId(UUID bookId) throws Exception {
    String response = mockMvc.perform(get("/books/{bookId}/instances", bookId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, new TypeReference<>() {
    });
  }

  public ErrorResponse getAllBookInstancesByBookIdNotFound(UUID bookId) throws Exception {
    String response = mockMvc.perform(get("/books/{bookId}/instances", bookId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public void deleteBookInstance(UUID bookId, UUID id) throws Exception {
    mockMvc.perform(delete("/books/{bookId}/instances/{id}", bookId, id))
        .andExpect(status().isNoContent());
  }

  public ErrorResponse deleteBookInstanceNotFound(UUID bookId, UUID id) throws Exception {
    String response = mockMvc.perform(delete("/books/{bookId}/instances/{id}", bookId, id))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }
}
