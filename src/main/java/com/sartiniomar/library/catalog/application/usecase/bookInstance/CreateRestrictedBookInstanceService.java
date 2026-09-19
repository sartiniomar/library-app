package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateRestrictedBookInstanceService implements CreateRestrictedBookInstanceUseCase {

  private final BookInstanceRepository repository;

  private final BookRepository bookRepository;

  public CreateRestrictedBookInstanceService(BookInstanceRepository repository, BookRepository bookRepository) {
    this.repository = repository;
    this.bookRepository = bookRepository;
  }

  @Override
  public BookInstance execute(CreateBookInstanceCommand command) {
    log.debug("Creating book instance in catalog. Book Id= {}", command.bookId());

    Book book = bookRepository.findById(command.bookId())
        .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + command.bookId().toString()));

    BookInstance bookInstance = BookInstance.restricted(book.getId());
    repository.save(bookInstance);

    log.debug("Book instance created. Book Instance Id= {}", bookInstance.getId());
    return bookInstance;
  }
}
