package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetBookByIsbnUseCaseImpl implements GetBookByIsbnUseCase {

  private final BookRepository repository;

  public GetBookByIsbnUseCaseImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book execute(String isbn) {
    log.debug("Finding book in catalog. ISBN= {}", isbn);

    Book book = repository.findByIsbn(isbn).orElseThrow(
        () -> new BookNotFoundException("Book with ISBN " + isbn + " not found"));

    log.debug("Book found. ISBN= {}", isbn);
    return book;
  }
}
