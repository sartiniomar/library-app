package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.CreateBookInstanceCommand;
import com.sartiniomar.library.lending.application.port.in.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;

public class CreateRestrictedBookInstanceService implements CreateRestrictedBookInstanceUseCase {

  private final BookInstanceRepository repository;

  public CreateRestrictedBookInstanceService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public void execute(CreateBookInstanceCommand command) {
    BookInstance instance = BookInstance.restricted(command.bookId());
    repository.save(instance);
  }
}
