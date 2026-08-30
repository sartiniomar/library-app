package com.sartiniomar.library.loan.infrastructure.web;

import com.sartiniomar.library.loan.application.port.in.reserve.ReserveCommand;
import com.sartiniomar.library.loan.application.port.in.reserve.ReserveUseCase;
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
  private final LoanMapper holdMapper;

  @PostMapping
  public ResponseEntity<HoldResponse> placeHold(@Valid @RequestBody PlaceHoldRequest request) {
    ReserveCommand command = holdMapper.placeHoldRequestToPlaceHoldCommand(request);
    return ResponseEntity.ok(holdMapper.holdToHoldResponse(useCase.execute(command)));
  }
}
