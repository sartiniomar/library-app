package com.sartiniomar.library.lending.infrastructure.web;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.infrastructure.mapper.HoldMapper;
import com.sartiniomar.library.lending.infrastructure.web.dto.HoldResponse;
import com.sartiniomar.library.lending.infrastructure.web.dto.PlaceHoldRequest;
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

  private final PlaceHoldUseCase useCase;
  private final HoldMapper holdMapper;

  @PostMapping
  public ResponseEntity<HoldResponse> placeHold(@Valid @RequestBody PlaceHoldRequest request) {
    PlaceHoldCommand command = holdMapper.placeHoldRequestToPlaceHoldCommand(request);
    return ResponseEntity.ok(holdMapper.holdToHoldResponse(useCase.execute(command)));
  }
}
