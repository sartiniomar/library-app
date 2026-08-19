package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import java.util.UUID;

public class GetBookByIdUseCaseImpl implements GetBookByIdUseCase {

  private final BookRepository repository;

  public GetBookByIdUseCaseImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book execute(UUID id) {
    return repository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id.toString()));
  }
}
