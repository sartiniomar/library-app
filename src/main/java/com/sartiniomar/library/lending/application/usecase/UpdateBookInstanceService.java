package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.UpdateBookInstanceCommand;
import com.sartiniomar.library.lending.application.port.in.UpdateBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookInstanceNotFoundException;

public class UpdateBookInstanceService implements UpdateBookInstanceUseCase {

  private final BookInstanceRepository repository;

  public UpdateBookInstanceService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public BookInstance execute(UpdateBookInstanceCommand cmd) {
    BookInstance bookInstance = repository.findById(cmd.id())
        .orElseThrow(() -> new BookInstanceNotFoundException(cmd.id().toString()));

    if (cmd.type() != null) {
      bookInstance.setType(cmd.type());
    }

    if (cmd.onHold() != null) {
      bookInstance.setOnHold(cmd.onHold());
    }

    return repository.save(bookInstance);
  }
}
