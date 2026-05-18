package com.sartiniomar.library.catalog.application.usecase;

import com.sartiniomar.library.catalog.application.port.in.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import java.util.UUID;

public class GetBookByIdByIdUseCaseImpl implements GetBookByIdUseCase {

  private final BookRepository repository;

  public GetBookByIdByIdUseCaseImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book get(UUID id) {
    return repository.findById(id).orElseThrow(() -> new BookNotFoundException("ID=" + id));
  }
}
