package com.sartiniomar.library.patron.infrastructure.web;

import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.CreateRegularPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.CreateResearcherPatronUseCase;
import com.sartiniomar.library.patron.application.port.in.DeletePatronUseCase;
import com.sartiniomar.library.patron.application.port.in.GetPatronByIdUseCase;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronUseCase;
import com.sartiniomar.library.patron.infrastructure.mapper.PatronMapper;
import com.sartiniomar.library.patron.infrastructure.web.dto.CreatePatronRequest;
import com.sartiniomar.library.patron.infrastructure.web.dto.PatronResponse;
import com.sartiniomar.library.patron.infrastructure.web.dto.UpdatePatronRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/patrons")
public class PatronController {

  private final CreateRegularPatronUseCase createRegularPatron;
  private final CreateResearcherPatronUseCase createResearcherPatron;
  private final GetPatronByIdUseCase getPatronById;
  private final UpdatePatronUseCase updatePatron;
  private final DeletePatronUseCase deletePatron;
  private final PatronMapper patronMapper;

  @PostMapping("/regular")
  public ResponseEntity<PatronResponse> createRegular(@Valid @RequestBody CreatePatronRequest request) {
    log.debug("Creating regular patron. Email= {}", request.email());
    CreatePatronCommand cmd = patronMapper.createPatronRequestToCreatePatronCommand(request);
    return ResponseEntity.ok(patronMapper.patronToPatronResponse(createRegularPatron.execute(cmd)));
  }

  @PostMapping("/researcher")
  public ResponseEntity<PatronResponse> createResearcher(@Valid @RequestBody CreatePatronRequest request) {
    log.debug("Creating researcher patron. Email= {}", request.email());
    CreatePatronCommand cmd = patronMapper.createPatronRequestToCreatePatronCommand(request);
    return ResponseEntity.ok(patronMapper.patronToPatronResponse(createResearcherPatron.execute(cmd)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PatronResponse> update(@PathVariable UUID id,
                                     @Valid @RequestBody UpdatePatronRequest request) {
    log.debug("Updating patron. Patron Id= {}", id);
    UpdatePatronCommand cmd = patronMapper.updatePatronRequestToUpdatePatronCommand(request, id);
    return ResponseEntity.ok(patronMapper.patronToPatronResponse(updatePatron.execute(cmd)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PatronResponse> getById(@PathVariable UUID id) {
    log.debug("Finding patron. Patron Id= {}", id);
    return ResponseEntity.ok(patronMapper.patronToPatronResponse(getPatronById.execute(id)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    log.debug("Deleting patron. Patron Id= {}", id);
    deletePatron.execute(id);
    return ResponseEntity.noContent().build();
  }
}
