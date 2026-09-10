package com.sartiniomar.library.loan.integration.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.loan.infrastructure.web.dto.LoanResponse;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanHttpHelper extends LibraryApplicationTests {

  public LoanResponse returnIsCreateWhenCreateReserve() throws Exception {
    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    String response =  mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, LoanResponse.class);
  }

  public ErrorResponse returnNotFoundWhenCreateReserve() throws Exception {
    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    String response = mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public ErrorResponse returnConflictWhenCreateReserve() throws Exception {
    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    String response = mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isConflict())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public LoanResponse returnOkWhenCancelReserve(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/cancels", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, LoanResponse.class);
  }

  public ErrorResponse returnNotFoundWhenCancelReserve(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/cancels", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public ErrorResponse returnConflictWhenCancelReserve(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/cancels", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public LoanResponse returnIsCreateWhenCreateCheckout() throws Exception {
    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    String response =  mockMvc.perform(post("/loans/checkouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, LoanResponse.class);
  }

  public ErrorResponse returnNotFoundWhenCreateCheckout() throws Exception {
    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    String response = mockMvc.perform(post("/loans/checkouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public ErrorResponse returnConflictWhenCreateCheckout() throws Exception {
    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    String response = mockMvc.perform(post("/loans/checkouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isConflict())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public LoanResponse returnIsCreateWhenCreateCheckoutFromReserve(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/checkouts", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, LoanResponse.class);
  }

  public ErrorResponse returnNotFoundWhenCreateCheckoutFromReserve(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/checkouts", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public ErrorResponse returnConflictWhenCheckoutFromReserve(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/checkouts", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public LoanResponse returnOkWhenReturnLoan(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/returns", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, LoanResponse.class);
  }

  public ErrorResponse returnNotFoundWhenReturnLoan(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/returns", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public ErrorResponse returnConflictWhenReturnLoan(UUID loanId) throws Exception {
    String response =  mockMvc.perform(post("/loans/{loanId}/returns", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public LoanResponse returnOkWhenGetLoanById(UUID loanId) throws Exception {
    String response =  mockMvc.perform(get("/loans/{loanId}", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, LoanResponse.class);
  }

  public ErrorResponse returnNotFoundWhenGetLoanById(UUID loanId) throws Exception {
    String response =  mockMvc.perform(get("/loans/{loanId}", loanId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, ErrorResponse.class);
  }

  public List<LoanResponse> returnOkWhenGetAllLoansByPatronId(UUID patronId) throws Exception {
    String response = mockMvc.perform(get("/loans")
            .param("patronId", patronId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
