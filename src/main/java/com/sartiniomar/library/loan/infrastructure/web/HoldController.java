package com.sartiniomar.library.loan.infrastructure.web;

import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapper;
import com.sartiniomar.library.loan.infrastructure.web.dto.HoldResponse;
import com.sartiniomar.library.loan.infrastructure.web.dto.PlaceHoldRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/holds")
public class HoldController {

  private final ReserveUseCase useCase;
  private final LoanMapper loanMapper;

  @PostMapping
  public ResponseEntity<HoldResponse> placeHold(@Valid @RequestBody PlaceHoldRequest request) {
    LoanCommand command = loanMapper.placeHoldRequestToLoanCommand(request);
    return ResponseEntity.ok(loanMapper.holdToHoldResponse(useCase.execute(command)));
  }
}
