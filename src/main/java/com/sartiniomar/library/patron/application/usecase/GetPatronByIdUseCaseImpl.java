package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.in.GetPatronByIdUseCase;
import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import java.util.UUID;

public class GetPatronByIdUseCaseImpl implements GetPatronByIdUseCase {

  private final PatronRepository repository;

  public GetPatronByIdUseCaseImpl(PatronRepository repository) {
    this.repository = repository;
  }

  @Override
  public Patron execute(UUID id) {
    return repository.findById(id).orElseThrow(() -> new PatronNotFoundException("ID=" + id));
  }
}
