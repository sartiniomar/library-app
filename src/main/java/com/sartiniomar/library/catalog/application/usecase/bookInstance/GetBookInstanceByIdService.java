package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j
public class GetBookInstanceByIdService implements GetBookInstanceByIdUseCase {

  private final BookInstanceRepository repository;

  public GetBookInstanceByIdService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public BookInstance execute(UUID command) {
    log.debug("Finding book instance in catalog. Book Instance Id= {}", command);

    BookInstance bookInstance = repository.findById(command).orElseThrow(
        () -> new BookInstanceNotFoundException("Book Instance not found with id: " + command));

    log.debug("Book instance found. Book Instance Id= {}", command);
    return bookInstance;
  }
}
