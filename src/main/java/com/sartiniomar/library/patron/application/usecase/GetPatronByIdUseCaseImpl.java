package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.in.GetPatronByIdUseCase;
import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j
public class GetPatronByIdUseCaseImpl implements GetPatronByIdUseCase {

  private final PatronRepository repository;

  public GetPatronByIdUseCaseImpl(PatronRepository repository) {
    this.repository = repository;
  }

  @Override
  public Patron execute(UUID id) {
    log.debug("Finding patron in catalog. Patron Id= {}", id);

    Patron patron = repository.findById(id).orElseThrow(
        () -> new PatronNotFoundException("Patron not found with id: " + id));

    log.debug("Patron found. Patron Id= {}", patron.getId());
    return patron;
  }
}
