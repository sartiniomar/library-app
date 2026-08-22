package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.CreateRegularPatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;

public class CreateRegularPatronUseCaseImpl implements CreateRegularPatronUseCase {

  private final PatronRepository repository;

  public CreateRegularPatronUseCaseImpl(PatronRepository repository) {
    this.repository = repository;
  }

  @Override
  public Patron execute(CreatePatronCommand command) {
    if (repository.existsByEmail(command.getEmail())) {
      throw new PatronAlreadyExistsException("Email " + command.getEmail() + " already exists");
    }
    Patron patron = Patron.regular(command.getName(), command.getEmail());
    return repository.save(patron);
  }
}
