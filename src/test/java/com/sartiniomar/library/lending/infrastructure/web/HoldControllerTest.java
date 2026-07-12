package com.sartiniomar.library.lending.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.infrastructure.web.dto.PlaceHoldRequest;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HoldControllerTest extends LibraryApplicationTests {

  @MockBean
  PlaceHoldUseCase useCase;

  @Test
  void shouldCallUseCase_whenRequestIsValid() throws Exception {
    BookInstance book = BookInstance.circulating(UUID.randomUUID());
    Patron patron = Patron.regular();

    PlaceHoldRequest request = new PlaceHoldRequest(book.getId(), patron.getId());

    mockMvc.perform(post("/holds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk());

    verify(useCase).execute(any(PlaceHoldCommand.class));
  }

  @Test
  void shouldReturn400_whenBookIdIsMissing() throws Exception {
    Patron patron = Patron.regular();
    PlaceHoldRequest request = new PlaceHoldRequest(null, patron.getId());

    mockMvc.perform(post("/holds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnValidationErrors() throws Exception {

    PlaceHoldRequest request = new PlaceHoldRequest(null, null);

    mockMvc.perform(post("/holds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.bookInstanceId").exists())
        .andExpect(jsonPath("$.errors.patronId").exists());
  }
}
