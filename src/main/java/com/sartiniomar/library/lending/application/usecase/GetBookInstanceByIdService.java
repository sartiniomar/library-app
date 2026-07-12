package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookInstanceNotFoundException;
import java.util.UUID;

public class GetBookInstanceByIdService implements GetBookInstanceByIdUseCase {

  private final BookInstanceRepository repository;

  public GetBookInstanceByIdService(BookInstanceRepository repository) {
    this.repository = repository;
  }

  @Override
  public BookInstance execute(UUID command) {
    return repository.findById(command).orElseThrow(() -> new BookInstanceNotFoundException("ID=" + command));
  }
}
