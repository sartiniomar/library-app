package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateBookUseCaseImpl implements UpdateBookUseCase {

  private final BookRepository bookRepository;

  public UpdateBookUseCaseImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public Book execute(UpdateBookCommand command) {
    log.debug("Updating book in catalog. Book Id= {}", command.id());

    Book existingBook = bookRepository.findById(command.id())
        .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + command.id()));

    if (command.isbn() != null &&
        !command.isbn().equals(existingBook.getIsbn())) {

      bookRepository.findByIsbn(command.isbn())
          .ifPresent(b -> {
            throw new BookAlreadyExistsException("ISBN " + command.isbn() + " already exists");
          });
    }

    existingBook.update(
        command.title(),
        command.author(),
        command.isbn()
    );

    bookRepository.save(existingBook);

    log.debug("Book updated. Book Id= {}", existingBook.getId());
    return existingBook;
  }
}
