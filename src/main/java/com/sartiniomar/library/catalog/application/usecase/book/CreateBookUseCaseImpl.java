package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.in.book.CreateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.book.CreateBookUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;

public class CreateBookUseCaseImpl implements CreateBookUseCase {

  private final BookRepository repository;

  public CreateBookUseCaseImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book execute(CreateBookCommand command) {

    if (repository.existsByIsbn(command.isbn())) {
      throw new BookAlreadyExistsException("ISBN " + command.isbn() + " already exists");
    }

    Book book = Book.create(
        command.title(),
        command.author(),
        command.isbn()
    );

    return repository.save(book);
  }
}
