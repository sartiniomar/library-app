package com.sartiniomar.library.patron.infrastructure.web;

import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.CreateRegularPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.CreateResearcherPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.DeletePatronUseCase;
import com.sartiniomar.library.patron.application.port.in.GetPatronByIdUseCase;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.web.request.CreatePatronRequest;
import com.sartiniomar.library.patron.infrastructure.web.request.UpdatePatronRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/patrons")
public class PatronController {

  private final CreateRegularPatronUseCase createRegularPatron;
  private final CreateResearcherPatronUseCase createResearcherPatron;
  private final GetPatronByIdUseCase getPatronById;
  private final UpdatePatronUseCase updatePatron;
  private final DeletePatronUseCase deletePatron;

  public PatronController(CreateRegularPatronUseCase createRegularPatron, CreateResearcherPatronUseCase createResearcherPatron, GetPatronByIdUseCase getPatronById, UpdatePatronUseCase updatePatron, DeletePatronUseCase deletePatron) {
    this.createRegularPatron = createRegularPatron;
    this.createResearcherPatron = createResearcherPatron;
    this.getPatronById = getPatronById;
    this.updatePatron = updatePatron;
    this.deletePatron = deletePatron;
  }

  @PostMapping("/regular")
  public ResponseEntity<Patron> createRegular(@Valid @RequestBody CreatePatronRequest createPatronRequest) {

    CreatePatronCommand cmd = new CreatePatronCommand(
        createPatronRequest.name(),
        createPatronRequest.email()
    );
    return ResponseEntity.ok(createRegularPatron.execute(cmd));
  }

  @PostMapping("/researcher")
  public ResponseEntity<Patron> createResearcher(@Valid @RequestBody CreatePatronRequest createPatronRequest) {

    CreatePatronCommand cmd = new CreatePatronCommand(
        createPatronRequest.name(),
        createPatronRequest.email()
    );
    return ResponseEntity.ok(createResearcherPatron.execute(cmd));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Patron> update(@PathVariable UUID id,
                                     @Valid @RequestBody UpdatePatronRequest updatePatronRequest) {

    UpdatePatronCommand cmd = new UpdatePatronCommand(
        id,
        updatePatronRequest.type(),
        updatePatronRequest.name(),
        updatePatronRequest.email()
    );
    return ResponseEntity.ok(updatePatron.execute(cmd));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Patron> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(getPatronById.execute(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    deletePatron.execute(id);
    return ResponseEntity.noContent().build();
  }
}
