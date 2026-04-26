package com.sartiniomar.library.lending.infrastructure.web;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.infrastructure.web.request.PlaceHoldRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/holds")
public class HoldController {

  private final PlaceHoldUseCase useCase;

  public HoldController(PlaceHoldUseCase useCase) {
    this.useCase = useCase;
  }

  @PostMapping
  public ResponseEntity<Void> placeHold(@Valid @RequestBody PlaceHoldRequest request) {

    PlaceHoldCommand command =
        new PlaceHoldCommand(request.getPatronId(), request.getBookId());

    useCase.execute(command);

    return ResponseEntity.ok().build();
  }
}
