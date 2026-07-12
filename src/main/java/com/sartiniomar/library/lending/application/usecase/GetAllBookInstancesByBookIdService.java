package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import java.util.List;
import java.util.UUID;

public class GetAllBookInstancesByBookIdService implements GetAllBookInstancesByBookIdUseCase {

  private final BookInstanceRepository repository;

  public GetAllBookInstancesByBookIdService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<BookInstance> execute(UUID command) {
    return repository.findAllByBookId(command);
  }
}
