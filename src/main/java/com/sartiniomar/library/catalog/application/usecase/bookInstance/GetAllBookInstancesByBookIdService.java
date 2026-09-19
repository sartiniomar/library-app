package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;

@Slf4j
public class GetAllBookInstancesByBookIdService implements GetAllBookInstancesByBookIdUseCase {

  private final BookInstanceRepository repository;

  private final BookRepository bookRepository;

  public GetAllBookInstancesByBookIdService(BookInstanceRepository repository, BookRepository bookRepository) {
    this.repository = repository;
    this.bookRepository = bookRepository;
  }

  @Override
  public List<BookInstance> execute(UUID command) {
    log.debug("Finding book instances in catalog. Book Id= {}", command);

    bookRepository.findById(command).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + command));
    List<BookInstance> bookInstances = repository.findAllByBookId(command);

    log.debug("Book instances found. Book Id= {}", command);
    return bookInstances;
  }
}
