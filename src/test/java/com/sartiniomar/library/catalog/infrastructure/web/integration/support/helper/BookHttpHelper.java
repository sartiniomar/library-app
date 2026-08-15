package com.sartiniomar.library.catalog.infrastructure.web.integration.support.helper;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookResponse;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import org.springframework.http.MediaType;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookHttpHelper extends LibraryApplicationTests {

  public BookResponse createBook() throws Exception {
    String bodyRequest = getContentFromFile("catalog/book/createBookRequest.json");

    String response = mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookResponse.class);
  }

  public ErrorResponse createBookDuplicateIsbn() throws Exception {
    String bodyRequest = getContentFromFile("catalog/book/createBookRequest.json");

    String response = mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public BookResponse updateBook(String id) throws Exception {
    String bodyRequest = getContentFromFile("catalog/book/updateBookRequest.json");

    String response = mockMvc.perform(put("/books/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookResponse.class);
  }

  public ErrorResponse updateBookDuplicateIsbn(UUID id) throws Exception {
    String bodyRequest = getContentFromFile("catalog/book/updateBookRequest.json");

    String response = mockMvc.perform(put("/books/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public ErrorResponse updateBookNotFound(UUID id) throws Exception {
    String bodyRequest = getContentFromFile("catalog/book/updateBookRequest.json");

    String response = mockMvc.perform(put("/books/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public BookResponse getById(String id) throws Exception {
    String response = mockMvc.perform(get("/books/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookResponse.class);
  }

  public BookResponse getByIsbn(String isbn) throws Exception {
    String response = mockMvc.perform(get("/books")
            .param("isbn", isbn)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, BookResponse.class);
  }

  public  ErrorResponse getByIsbnNotFound(String isbn) throws Exception {
    String response = mockMvc.perform(get("/books")
            .param("isbn", isbn)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public void deleteBook(String id) throws Exception {
    mockMvc.perform(delete("/books/{id}", id))
        .andExpect(status().isNoContent());
  }

  public ErrorResponse deleteBookNotFound(String id) throws Exception {
    String response = mockMvc.perform(delete("/books/{id}", id))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }
}
