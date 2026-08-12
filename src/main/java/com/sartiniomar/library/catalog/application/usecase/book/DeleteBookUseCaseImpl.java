package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.in.book.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import java.util.UUID;

public class DeleteBookUseCaseImpl implements DeleteBookUseCase {

  private final BookRepository bookRepository;

  public DeleteBookUseCaseImpl(BookRepository repository) {
    this.bookRepository = repository;
  }

  @Override
  public void execute(UUID id) {
    bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id.toString()));
    bookRepository.delete(id);
  }
}
