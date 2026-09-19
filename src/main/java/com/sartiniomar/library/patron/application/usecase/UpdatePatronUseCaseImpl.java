package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronUseCase;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdatePatronUseCaseImpl implements UpdatePatronUseCase {

  private final PatronRepository repository;

  public UpdatePatronUseCaseImpl(PatronRepository repository) {
    this.repository = repository;
  }

  @Override
  public Patron execute(UpdatePatronCommand command) {
    log.debug("Updated patron in catalog. Patron Id= {}", command.id());

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

    log.debug("Patron updated. Patron Id= {}", patron.getId());
    return repository.save(patron);
  }
}
