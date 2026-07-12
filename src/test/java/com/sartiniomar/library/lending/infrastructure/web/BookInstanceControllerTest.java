package com.sartiniomar.library.lending.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.lending.application.port.in.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.DeleteBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.lending.application.port.in.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.lending.application.port.in.UpdateBookInstanceUseCase;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookInstanceNotFoundException;
import com.sartiniomar.library.lending.domain.book.BookType;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceMapperImpl;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository.BookInstanceSpringDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({BookInstanceMapperImpl.class})
class BookInstanceControllerTest extends LibraryApplicationTests {

  @Autowired
  private BookInstanceMapper mapper;

  @MockBean
  BookInstanceSpringDataRepository bookInstanceSpringDataRepository;

  @MockBean
  CreateCirculatingBookInstanceUseCase createCirculatingBookInstanceUseCase;

  @MockBean
  CreateRestrictedBookInstanceUseCase createRestrictedBookInstanceUseCase;

  @MockBean
  GetBookInstanceByIdUseCase getBookInstanceByIdUseCase;

  @MockBean
  GetAllBookInstancesByBookIdUseCase getAllBookInstancesByBookIdUseCase;

  @MockBean
  UpdateBookInstanceUseCase updateBookInstanceUseCase;

  @MockBean
  DeleteBookInstanceUseCase deleteBookInstanceUseCase;

  @Test
  void shouldCreateCirculatingBookInstance() throws Exception {
    UUID bookId = UUID.randomUUID();

    BookInstance bookInstance = BookInstance.circulating(bookId);

    when(createCirculatingBookInstanceUseCase.execute(any()))
        .thenReturn(bookInstance);

    mockMvc.perform(post("/books/" + bookId + "/instances/circulating")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookInstance.getId().toString()))
        .andExpect(jsonPath("$.bookId").value(bookId.toString()))
        .andExpect(jsonPath("$.type").value("CIRCULATING"))
        .andExpect(jsonPath("$.onHold").value(false));
  }

  @Test
  void shouldReturnBadRequestForInvalidInputCreateCirculatingBookInstance() throws Exception {
    String bookId = "invalid";

    mockMvc.perform(post("/books/" + bookId + "/instances/circulating")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldCreateRestrictedBookInstance() throws Exception {
    UUID bookId = UUID.randomUUID();

    BookInstance bookInstance = BookInstance.restricted(bookId);

    when(createRestrictedBookInstanceUseCase.execute(any()))
        .thenReturn(bookInstance);

    mockMvc.perform(post("/books/" + bookId + "/instances/restricted")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookInstance.getId().toString()))
        .andExpect(jsonPath("$.bookId").value(bookId.toString()))
        .andExpect(jsonPath("$.type").value("RESTRICTED"))
        .andExpect(jsonPath("$.onHold").value(false));
  }

  @Test
  void shouldReturnBadRequestForInvalidInputCreateRestrictedBookInstance() throws Exception {
    String bookId = "invalid";

    mockMvc.perform(post("/books/" + bookId + "/instances/restricted")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldUpdateBookInstance() throws Exception {
    UUID bookId = UUID.randomUUID();

    BookInstance bookInstance = BookInstance.circulating(bookId);
    bookInstanceSpringDataRepository.save(mapper.toEntity(bookInstance));

    BookInstance updatedBookInstance = new BookInstance(bookInstance.getId(), bookId, BookType.RESTRICTED, true);
    when(updateBookInstanceUseCase.execute(any())).thenReturn(updatedBookInstance);

    String bodyRequest = """
        {
          "type": "RESTRICTED",
          "onHold": "true"
        }
        """;
    mockMvc.perform(put("/books/" + bookId + "/instances/" + bookInstance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookInstance.getId().toString()))
        .andExpect(jsonPath("$.bookId").value(bookId.toString()))
        .andExpect(jsonPath("$.type").value("RESTRICTED"))
        .andExpect(jsonPath("$.onHold").value(true));

  }

  @Test
  void shouldReturnBadRequestForInvalidInputUpdateBookInstance() throws Exception {
    UUID bookId = UUID.randomUUID();

    BookInstance bookInstance = BookInstance.circulating(bookId);
    bookInstanceSpringDataRepository.save(mapper.toEntity(bookInstance));

    String bodyRequest = """
        {
          "type": "RESTRICTED",
          "onHold": "true"
        }
        """;
    mockMvc.perform(put("/books/" + bookId + "/instances/invalid")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isBadRequest());

  }

  @Test
  void shouldDeleteBookInstance() throws Exception {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);
    bookInstanceSpringDataRepository.save(mapper.toEntity(bookInstance));

    mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + bookId + "/instances/" + bookInstance.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookInstanceOnDelete() throws Exception {
    UUID bookId = UUID.randomUUID();
    UUID instanceId = UUID.randomUUID();

    doThrow(new BookInstanceNotFoundException("Book instance not found"))
        .when(deleteBookInstanceUseCase).execute(any());

    mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + bookId + "/instances/" + instanceId))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForInvalidUuidOnDelete() throws Exception {
    String invalidId = "invalid-uuid";

    mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + invalidId + "/instances/" + invalidId))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldGetBookInstanceById() throws Exception {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);
    bookInstanceSpringDataRepository.save(mapper.toEntity(bookInstance));

    when(getBookInstanceByIdUseCase.execute(any())).thenReturn(bookInstance);

    mockMvc.perform(MockMvcRequestBuilders.get("/books/" + bookId + "/instances/" + bookInstance.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookInstance.getId().toString()))
        .andExpect(jsonPath("$.bookId").value(bookId.toString()))
        .andExpect(jsonPath("$.type").value("CIRCULATING"))
        .andExpect(jsonPath("$.onHold").value(false));
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookInstanceOnGetById() throws Exception {
    UUID bookId = UUID.randomUUID();
    UUID instanceId = UUID.randomUUID();

    doThrow(new BookInstanceNotFoundException("Book instance not found"))
        .when(getBookInstanceByIdUseCase).execute(any());

    mockMvc.perform(MockMvcRequestBuilders.get("/books/" + bookId + "/instances/" + instanceId))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForInvalidInputGetBookInstanceById() throws Exception {
    String invalidId = "invalid-uuid";

    mockMvc.perform(MockMvcRequestBuilders.get("/books/" + invalidId + "/instances/" + invalidId))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldGetAllBookInstancesByBookId() throws Exception {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = BookInstance.circulating(bookId);
    bookInstanceSpringDataRepository.save(mapper.toEntity(bookInstance));
    BookInstance bookInstance2 = BookInstance.circulating(bookId);
    bookInstanceSpringDataRepository.save(mapper.toEntity(bookInstance2));

    UUID otherBookId = UUID.randomUUID();
    BookInstance otherBookInstance = BookInstance.circulating(otherBookId);
    bookInstanceSpringDataRepository.save(mapper.toEntity(otherBookInstance));

    when(getAllBookInstancesByBookIdUseCase.execute(any())).thenReturn(java.util.List.of(bookInstance, bookInstance2));

    mockMvc.perform(MockMvcRequestBuilders.get("/books/" + bookId + "/instances"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", notNullValue()))
        .andExpect(jsonPath("$[1].id", notNullValue()));
  }

  @Test
  void shouldReturnNotFoundForNonExistingBookInstanceOnGetAllById() throws Exception {
    UUID bookId = UUID.randomUUID();

    doThrow(new BookInstanceNotFoundException("Book not found"))
        .when(getBookInstanceByIdUseCase).execute(any());

    mockMvc.perform(MockMvcRequestBuilders.get("/books/" + bookId + "/instances/"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestForInvalidInputGetAllBookInstancesByBookId() throws Exception {
    String invalidId = "invalid-uuid";

    mockMvc.perform(MockMvcRequestBuilders.get("/books/" + invalidId + "/instances"))
        .andExpect(status().isBadRequest());
  }
}
