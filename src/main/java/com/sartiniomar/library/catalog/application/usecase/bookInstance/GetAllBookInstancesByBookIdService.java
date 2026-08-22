package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import java.util.List;
import java.util.UUID;

public class GetAllBookInstancesByBookIdService implements GetAllBookInstancesByBookIdUseCase {

  private final BookInstanceRepository repository;

  private final BookRepository bookRepository;

  public GetAllBookInstancesByBookIdService(BookInstanceRepository repository, BookRepository bookRepository) {
    this.repository = repository;
    this.bookRepository = bookRepository;
  }

  @Override
  public List<BookInstance> execute(UUID command) {
    bookRepository.findById(command).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + command));
    return repository.findAllByBookId(command);
  }
}
