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
  public void execute(UpdateBookInstanceCommand cmd) {
    BookInstance instance = repository.findById(cmd.id())
        .orElseThrow(() -> new BookInstanceNotFoundException(cmd.id().toString()));

    if (cmd.bookId() != null) {
      instance.setBookId(cmd.bookId());
    }

    if (cmd.type() != null) {
      instance.setType(cmd.type());
    }

    repository.save(instance);
  }
}
