package com.sartiniomar.library.lending.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.domain.hold.Hold;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.lending.infrastructure.mapper.PatronHoldMapper;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.repository.BookInstanceJpaRepository;
import com.sartiniomar.library.lending.infrastructure.web.dto.PlaceHoldRequest;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.lending.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.repository.PatronJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HoldControllerTest extends LibraryApplicationTests {

  @MockBean
  PlaceHoldUseCase useCase;

  @MockBean
  BookInstanceJpaRepository bookInstanceSpringDataRepository;

  @MockBean
  PatronJpaRepository patronSpringDataRepository;

  @Autowired
  private BookInstanceMapper bookInstanceMapper;

  @Autowired
  private PatronHoldMapper patronHoldMapper;

  @Test
  void shouldCallUseCase_whenRequestIsValid() throws Exception {
    BookInstance bookInstance = BookInstance.circulating(UUID.randomUUID());
    bookInstanceSpringDataRepository.save(bookInstanceMapper.toEntity(bookInstance));

    Patron patron = Patron.regular();
    patronSpringDataRepository.save(patronHoldMapper.toEntity(patron));

    PlaceHoldRequest request = new PlaceHoldRequest(bookInstance.getId(), patron.getId());

    Hold hold = new Hold(patron.getId(), bookInstance.getId());

    when(useCase.execute(any()))
        .thenReturn(hold);

    mockMvc.perform(post("/holds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.bookInstanceId").value(bookInstance.getId().toString()))
        .andExpect(jsonPath("$.patronId").value(patron.getId().toString()));

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

  /*@Test
  void shouldReturnValidationErrors() throws Exception {

    PlaceHoldRequest request = new PlaceHoldRequest(null, null);

    mockMvc.perform(post("/holds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value("bookInstanceId must not be null"))
        .andExpect(jsonPath("$.errors[1].description").value("patronId must not be null"));
  }*/
}
