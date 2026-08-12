package com.sartiniomar.library.catalog.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.catalog.application.port.in.book.CreateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.book.CreateBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookUseCase;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.catalog.support.builder.BookTestDataBuilder;
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

  private static Stream<Arguments> provideDataForGroupBadRequest() {
    return Stream.of(
        Arguments.of("catalog/book/createBookRequestWithTitleBlank.json", "title is required"),
        Arguments.of("catalog/book/createBookRequestWithAuthorBlank.json", "author is required"),
        Arguments.of("catalog/book/createBookRequestWithIsbnBlank.json", "isbn is required"),
        Arguments.of("catalog/book/createBookRequestWithTitleNull.json", "title is required"),
        Arguments.of("catalog/book/createBookRequestWithAuthorNull.json", "author is required"),
        Arguments.of("catalog/book/createBookRequestWithIsbnNull.json", "isbn is required")
    );
  }

  @Test
  @SneakyThrows
  void shouldCreateBookResponse() {
    ArgumentCaptor<CreateBookCommand> createBookCommandArgumentCaptor = ArgumentCaptor.forClass(CreateBookCommand.class);

    Book book = new BookTestDataBuilder().buildDefault();

    when(createBookUseCase.execute(createBookCommandArgumentCaptor.capture())).thenReturn(book);

    String bodyRequest = getContentFromFile("catalog/book/createBookRequest.json");

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Title"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.isbn").value("123"));

    assertEquals("Title", createBookCommandArgumentCaptor.getValue().title());
    assertEquals("Author", createBookCommandArgumentCaptor.getValue().author());
    assertEquals("123", createBookCommandArgumentCaptor.getValue().isbn());

    verify(createBookUseCase, times(1)).execute(createBookCommandArgumentCaptor.getValue());
  }

  @ParameterizedTest
  @MethodSource("provideDataForGroupBadRequest")
  @SneakyThrows
  void shouldReturnBadRequestForInvalidInput(String requestFilePath, String description) {
    String bodyRequest = getContentFromFile(requestFilePath);

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value(description));

    verify(createBookUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForDuplicateIsbn() {
    when(createBookUseCase.execute(any())).thenThrow(new BookAlreadyExistsException("ISBN already exists"));

    String bodyRequest = getContentFromFile("catalog/book/createBookRequest.json");

    mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description").value("ISBN already exists"));

    verify(createBookUseCase, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnUpdateBookResponse() {
    ArgumentCaptor<UpdateBookCommand> updateBookCommandArgumentCaptor = ArgumentCaptor.forClass(UpdateBookCommand.class);

    Book book = new BookTestDataBuilder().build("Other Title", "Other Author", "Other 123");

    when(updateBookUseCase.execute(updateBookCommandArgumentCaptor.capture())).thenReturn(book);

    String bodyRequest = getContentFromFile("catalog/book/updateBookRequest.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", book.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(book.getId().toString()))
        .andExpect(jsonPath("$.title").value("Other Title"))
        .andExpect(jsonPath("$.author").value("Other Author"))
        .andExpect(jsonPath("$.isbn").value("Other 123"));

    assertEquals("Other Title", updateBookCommandArgumentCaptor.getValue().title());
    assertEquals("Other Author", updateBookCommandArgumentCaptor.getValue().author());
    assertEquals("Other 123", updateBookCommandArgumentCaptor.getValue().isbn());

    verify(updateBookUseCase, times(1)).execute(updateBookCommandArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForDuplicateIsbnOnUpdate() {
    UUID id = UUID.randomUUID();

    when(updateBookUseCase.execute(any())).thenThrow(new BookAlreadyExistsException("ISBN already exists"));

    String bodyRequest = getContentFromFile("catalog/book/updateBookRequest.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description").value("ISBN already exists"));

    verify(updateBookUseCase, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnUpdate() {
    String bodyRequest = getContentFromFile("catalog/book/updateBookRequest.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", "invalid-uuid")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(updateBookUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookOnUpdate() {
    UUID id = UUID.randomUUID();

    when(updateBookUseCase.execute(any())).thenThrow(new BookNotFoundException("Book not found"));

    String bodyRequest = getContentFromFile("catalog/book/updateBookRequest.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book not found"));

    verify(updateBookUseCase, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldGetById() {
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

    Book book = new BookTestDataBuilder().buildDefault();

    when(getBookByIdUseCase.execute(uuidArgumentCaptor.capture())).thenReturn(book);

    mockMvc.perform(get("/books/{id}", book.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(book.getId().toString()))
        .andExpect(jsonPath("$.title").value("Title"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.isbn").value("123"));

    assertEquals(book.getId(), uuidArgumentCaptor.getValue());

    verify(getBookByIdUseCase, times(1)).execute(uuidArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookOnGetById() {
    UUID inexistentId = UUID.randomUUID();

    when(getBookByIdUseCase.execute(inexistentId))
        .thenThrow(new BookNotFoundException("Book not found"));

    mockMvc.perform(get("/books/{id}", inexistentId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book not found"));

    verify(getBookByIdUseCase, times(1)).execute(inexistentId);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnGetById() {
    String invalidId = "invalid-uuid";

    mockMvc.perform(get("/books/{id}", invalidId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(getBookByIdUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldGetByIsbn() {
    Book book = new BookTestDataBuilder().buildDefault();

    when(getBookByIsbnUseCase.execute(book.getIsbn()))
        .thenReturn(book);

    mockMvc.perform(get("/books")
            .param("isbn", book.getIsbn())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(book.getId().toString()))
        .andExpect(jsonPath("$.title").value("Title"))
        .andExpect(jsonPath("$.author").value("Author"))
        .andExpect(jsonPath("$.isbn").value("123"));

    verify(getBookByIsbnUseCase, times(1)).execute(book.getIsbn());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookOnGetByIsbn() {
    Book book = new BookTestDataBuilder().buildDefault();

    when(getBookByIsbnUseCase.execute(book.getIsbn()))
        .thenThrow(new BookNotFoundException("Book not found"));

    mockMvc.perform(get("/books")
            .param("isbn", book.getIsbn())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book not found"));

    verify(getBookByIsbnUseCase, times(1)).execute(book.getIsbn());
  }

  @Test
  @SneakyThrows
  void shouldDeleteBook() {
    UUID id = UUID.randomUUID();

    mockMvc.perform(delete("/books/{id}", id))
        .andExpect(status().isNoContent());

    verify(deleteBookUseCase, times(1)).execute(id);
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookOnDelete() {
    UUID id = UUID.randomUUID();

    doThrow(new BookNotFoundException("Book not found with id: " + id))
        .when(deleteBookUseCase).execute(any());

    mockMvc.perform(delete("/books/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book not found with id: " + id));

    verify(deleteBookUseCase, times(1)).execute(id);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnDelete() {
    String invalidId = "invalid-uuid";

    mockMvc.perform(delete("/books/{id}", invalidId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(deleteBookUseCase, never()).execute(any());
  }
}
