package com.sartiniomar.library.catalog.application.usecase;

import com.sartiniomar.library.catalog.application.port.in.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;

public class GetBookByIsbnUseCaseImpl implements GetBookByIsbnUseCase {

  private final BookRepository repository;

  public GetBookByIsbnUseCaseImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book get(String isbn) {
    return repository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException("ISBN=" + isbn));
  }
}
