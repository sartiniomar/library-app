package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.CreateResearcherPatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateResearcherPatronUseCaseImpl implements CreateResearcherPatronUseCase {

  private final PatronRepository repository;

  public CreateResearcherPatronUseCaseImpl(PatronRepository repository) {
    this.repository = repository;
  }

  @Override
  public Patron execute(CreatePatronCommand command) {
    log.debug("Creating patron in catalog. Email= {}", command.getEmail());

    if (repository.existsByEmail(command.getEmail())) {
      throw new PatronAlreadyExistsException("Email " + command.getEmail() + " already exists");
    }

    Patron patron = Patron.researcher(command.getName(), command.getEmail());

    log.debug("Patron created. Patron Id= {}", patron.getId());
    return repository.save(patron);
  }
}
