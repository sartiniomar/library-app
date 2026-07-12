package com.sartiniomar.library.catalog.application.usecase;

import com.sartiniomar.library.catalog.application.port.in.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import java.util.UUID;

public class DeleteBookUseCaseImpl implements DeleteBookUseCase {

  private final BookRepository bookRepository;

  public DeleteBookUseCaseImpl(BookRepository repository) {
    this.bookRepository = repository;
  }

  @Override
  public void delete(UUID id) {
    bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("UUID=" + id.toString()));
    bookRepository.delete(id);
  }
}
