package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.CreateBookInstanceCommand;
import com.sartiniomar.library.lending.application.port.in.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;

public class CreateCirculatingBookInstanceService implements CreateCirculatingBookInstanceUseCase {

  private final BookInstanceRepository repository;

  public CreateCirculatingBookInstanceService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public BookInstance execute(CreateBookInstanceCommand command) {
    BookInstance bookInstance = BookInstance.circulating(command.bookId());
    repository.save(bookInstance);
    return bookInstance;
  }
}