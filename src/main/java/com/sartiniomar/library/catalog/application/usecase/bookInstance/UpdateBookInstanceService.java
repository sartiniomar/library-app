package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateBookInstanceService implements UpdateBookInstanceUseCase {

  private final BookInstanceRepository repository;

  public UpdateBookInstanceService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public BookInstance execute(UpdateBookInstanceCommand cmd) {
    log.debug("Updating book instance in catalog. Book Instance Id= {}", cmd.id());

    BookInstance bookInstance = repository.findById(cmd.id())
        .orElseThrow(() -> new BookInstanceNotFoundException("Book Instance not found with id: " + cmd.id().toString()));

    bookInstance.update(cmd.type());
    BookInstance updatedBookInstance = repository.save(bookInstance);

    log.debug("Book instance updated. Book Instance Id= {}", cmd.id());
    return updatedBookInstance;
  }
}
