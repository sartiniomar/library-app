package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import java.util.UUID;

public class GetBookInstanceByIdService implements GetBookInstanceByIdUseCase {

  private final BookInstanceRepository repository;

  public GetBookInstanceByIdService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public BookInstance execute(UUID command) {
    return repository.findById(command).orElseThrow(() -> new BookInstanceNotFoundException("Book Instance not found with id: " + command));
  }
}
