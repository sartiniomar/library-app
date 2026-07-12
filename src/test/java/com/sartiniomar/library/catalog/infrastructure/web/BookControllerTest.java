package com.sartiniomar.library.catalog.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.catalog.application.port.in.CreateBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.in.UpdateBookUseCase;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest extends LibraryApplicationTests {

  @MockBean
  private CreateBookUseCase createBookUseCase;

  @MockBean
  private UpdateBookUseCase updateBookUseCase;

  @MockBean
  private GetBookByIdUseCase getBookByIdUseCase;

  @MockBean
  private GetBookByIsbnUseCase getBookByIsbnUseCase;

  @MockBean
  private DeleteBookUseCase deleteBookUseCase;

  @Test
  void shouldCreateBook() throws Exception {

    UUID id = UUID.randomUUID();

    Book book = new Book(id, "Title", "Author", "123");

    when(createBookUseCase.create(any()))
        .thenReturn(book);

    String json = """
        {
          "title": "Title",
          "author": "Author",
          "isbn": "123"
        }
        """;

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Title"));
  }

  @Test
  void shouldReturnBadRequestForInvalidInput() throws Exception {
    String json = """
        {
          "title": "",
          "author": "Author",
          "isbn": "123"
        }
        """;

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForMissingFields() throws Exception {
    String json = """
        {
          "author": "Author",
          "isbn": "123"
        }
        """;

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForNullFields() throws Exception {
    String json = """
        {
          "title": null,
          "author": "Author",
          "isbn": "123"
        }
        """;

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForDuplicateIsbn() throws Exception {
    when(createBookUseCase.create(any()))
        .thenThrow(new BookAlreadyExistsException("ISBN already exists"));

    String json = """
        {
          "title": "Title",
          "author": "Author",
          "isbn": "123"
        }
        """;

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void shouldUpdateBook() throws Exception {
    UUID id = UUID.randomUUID();
    Book book = new Book(id, "Other Title", "Other Author", "Other 123");

    when(updateBookUseCase.update(any()))
        .thenReturn(book);

    String json = """
        {
          "title": "Other Title",
          "author": "Other Author",
          "isbn": "Other 123"
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/books/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Other Title"))
        .andExpect(jsonPath("$.author").value("Other Author"))
        .andExpect(jsonPath("$.isbn").value("Other 123"));

  }

  @Test
  void shouldReturnBadRequestForDuplicateIsbnOnUpdate() throws Exception {
    UUID id = UUID.randomUUID();

    when(updateBookUseCase.update(any()))
        .thenThrow(new BookAlreadyExistsException("ISBN already exists"));

    String json = """
        {
          "title": "Other Title",
          "author": "Other Author",
          "isbn": "Other 123"
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/books/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void shouldReturnBadRequestForInvalidUuidOnUpdate() throws Exception {
    String invalidId = "invalid-uuid";

    String json = """
        {
          "title": "Other Title",
          "author": "Other Author",
          "isbn": "Other 123"
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/books/" + invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnUpdate() throws Exception {
    UUID id = UUID.randomUUID();

    when(updateBookUseCase.update(any()))
        .thenThrow(new BookNotFoundException("Book not found"));

    String json = """
        {
          "title": "Other Title",
          "author": "Other Author",
          "isbn": "Other 123"
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/books/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldGetById() throws Exception {
    UUID id = UUID.randomUUID();
    Book book = new Book(id, "Title", "Author", "123");

    when(getBookByIdUseCase.get(any()))
        .thenReturn(book);

    mockMvc.perform(get("/books/" + id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.title").value("Title"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.isbn").value("123"));
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnGetById() throws Exception {
    UUID inexistentId = UUID.randomUUID();

    when(getBookByIdUseCase.get(inexistentId))
        .thenThrow(new BookNotFoundException("Book not found"));

    mockMvc.perform(get("/books/" + inexistentId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForInvalidUuidOnGetById() throws Exception {
    String invalidId = "invalid-uuid";

    mockMvc.perform(get("/books/" + invalidId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldGetByIsbn() throws Exception {
    String isbn = "123";
    Book book = new Book(UUID.randomUUID(), "Title", "Author", isbn);

    when(getBookByIsbnUseCase.get(isbn))
        .thenReturn(book);

    mockMvc.perform(get("/books")
            .param("isbn", isbn)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(book.getId().toString()))
        .andExpect(jsonPath("$.title").value("Title"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.isbn").value("123"));
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnGetByIsbn() throws Exception {
    String inexistentIsbn = "inexistent-isbn";

    when(getBookByIsbnUseCase.get(inexistentIsbn))
        .thenThrow(new BookNotFoundException("Book not found"));

    mockMvc.perform(get("/books")
            .param("isbn", inexistentIsbn)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteBook() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + id))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookOnDelete() throws Exception {
    UUID id = UUID.randomUUID();

    doThrow(new BookNotFoundException("Book not found"))
        .when(deleteBookUseCase).delete(any());

    mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + id))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForInvalidUuidOnDelete() throws Exception {
    String invalidId = "invalid-uuid";

    mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + invalidId))
        .andExpect(status().isBadRequest());
  }
}
