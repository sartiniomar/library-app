package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.in.DeletePatronUseCase;
import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j
public class DeletePatronUseCaseImpl implements DeletePatronUseCase {

  private final PatronRepository patronRepository;

  public DeletePatronUseCaseImpl(PatronRepository patronRepository) {
    this.patronRepository = patronRepository;
  }

  @Override
  public void execute(UUID id) {
    log.debug("Deleting patron in catalog. Patron Id= {}", id);

    Patron existingPatron = patronRepository.findById(id)
        .orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + id.toString()));

    patronRepository.delete(existingPatron.getId());

    log.debug("Patron deleted. Patron Id= {}", id);
  }
}
