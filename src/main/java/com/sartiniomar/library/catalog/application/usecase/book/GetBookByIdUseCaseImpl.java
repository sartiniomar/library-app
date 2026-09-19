package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j
public class GetBookByIdUseCaseImpl implements GetBookByIdUseCase {

  private final BookRepository repository;

  public GetBookByIdUseCaseImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book execute(UUID id) {
    log.debug("Finding book in catalog. Book Id= {}", id);

    Book book = repository.findById(id).orElseThrow(
        () -> new BookNotFoundException("Book not found with id: " + id.toString()));

    log.debug("Book found. Book Id= {}", id);
    return book;
  }
}
