package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;

public class UpdateBookInstanceService implements UpdateBookInstanceUseCase {

  private final BookInstanceRepository repository;

  public UpdateBookInstanceService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public BookInstance execute(UpdateBookInstanceCommand cmd) {
    BookInstance bookInstance = repository.findById(cmd.id())
        .orElseThrow(() -> new BookInstanceNotFoundException("Book Instance not found with id: " + cmd.id().toString()));

    if (cmd.type() != null) {
      bookInstance.setType(cmd.type());
    }

    return repository.save(bookInstance);
  }
}
