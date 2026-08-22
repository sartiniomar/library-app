package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;

public class CreateCirculatingBookInstanceService implements CreateCirculatingBookInstanceUseCase {

  private final BookInstanceRepository repository;

  private final BookRepository bookRepository;

  public CreateCirculatingBookInstanceService(BookInstanceRepository repository, BookRepository bookRepository) {
    this.repository = repository;
    this.bookRepository = bookRepository;
  }

  @Override
  public BookInstance execute(CreateBookInstanceCommand command) {
    Book book = bookRepository.findById(command.bookId())
        .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + command.bookId().toString()));
    BookInstance bookInstance = BookInstance.circulating(book.getId());
    repository.save(bookInstance);
    return bookInstance;
  }
}