package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.CreateResearcherPatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;

public class CreateResearcherPatronUseCaseImpl implements CreateResearcherPatronUseCase {

  private final PatronRepository repository;

  public CreateResearcherPatronUseCaseImpl(PatronRepository repository) {
    this.repository = repository;
  }

  @Override
  public Patron execute(CreatePatronCommand command) {
    if (repository.existsByEmail(command.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }

    Patron patron = Patron.researcher(command.getName(), command.getEmail());

    return repository.save(patron);
  }
}
