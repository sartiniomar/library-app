package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;

public class UpdatePatronUseCaseImpl implements UpdatePatronUseCase {

  private final PatronRepository repository;

  public UpdatePatronUseCaseImpl(PatronRepository repository) {
    this.repository = repository;
  }

  @Override
  public Patron execute(UpdatePatronCommand command) {

    Patron patron = repository.findById(command.id())
      .orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + command.id()));

    if (command.email() != null &&
        !command.email().equals(patron.getEmail())) {

      repository.findByEmail(command.email())
          .ifPresent(b -> {
            throw new PatronAlreadyExistsException("Email " + command.email() + " already exists");
          });
    }

    patron.update(command.type(), command.name(), command.email());

    return repository.save(patron);
  }
}
