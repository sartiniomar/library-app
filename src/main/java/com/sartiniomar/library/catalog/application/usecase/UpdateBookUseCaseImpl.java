package com.sartiniomar.library.catalog.application.usecase;

import com.sartiniomar.library.catalog.application.port.in.UpdateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.UpdateBookUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;

public class UpdateBookUseCaseImpl implements UpdateBookUseCase {

  private final BookRepository bookRepository;

  public UpdateBookUseCaseImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public Book update(UpdateBookCommand command) {

    Book existingBook = bookRepository.findById(command.id())
        .orElseThrow(() -> new BookNotFoundException("UUID=" + command.id()));

    if (command.isbn() != null &&
        !command.isbn().equals(existingBook.getIsbn())) {

      bookRepository.findByIsbn(command.isbn())
          .ifPresent(b -> {
            throw new BookAlreadyExistsException(command.isbn());
          });
    }

    existingBook.update(
        command.title(),
        command.author(),
        command.isbn()
    );

    return bookRepository.save(existingBook);
  }
}
