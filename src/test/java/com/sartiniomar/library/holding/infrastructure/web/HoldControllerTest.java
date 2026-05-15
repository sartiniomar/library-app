package com.sartiniomar.library.holding.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sartiniomar.library.holding.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.holding.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.holding.infrastructure.web.request.PlaceHoldRequest;
import com.sartiniomar.library.holding.model.book.BookInstance;
import com.sartiniomar.library.holding.model.patron.Patron;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HoldControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  PlaceHoldUseCase useCase;

  @Test
  void shouldCallUseCase_whenRequestIsValid() throws Exception {
    BookInstance book = BookInstance.circulating("book-1");
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
        .andExpect(jsonPath("$.errors.bookId").exists())
        .andExpect(jsonPath("$.errors.patronId").exists());
  }
}
