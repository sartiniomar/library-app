package com.sartiniomar.library.catalog.infrastructure.config;

import com.sartiniomar.library.catalog.application.port.in.book.CreateBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.application.usecase.book.CreateBookUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.book.DeleteBookUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.book.GetBookByIdUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.book.GetBookByIsbnUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.book.UpdateBookUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookConfig {

  @Bean
  public CreateBookUseCase createBookUseCase(BookRepository bookRepository) {
    return new CreateBookUseCaseImpl(bookRepository);
  }

  @Bean
  public UpdateBookUseCase updateBookUseCase(BookRepository bookRepository) {
    return new UpdateBookUseCaseImpl(bookRepository);
  }

  @Bean
  public GetBookByIdUseCase getBookByIdUseCase(BookRepository bookRepository) {
    return new GetBookByIdUseCaseImpl(bookRepository);
  }

  @Bean
  public GetBookByIsbnUseCase getBookByIsbnUseCase(BookRepository bookRepository) {
    return new GetBookByIsbnUseCaseImpl(bookRepository);
  }

  @Bean
  public DeleteBookUseCase deleteBookUseCase(BookRepository bookRepository) {
    return new DeleteBookUseCaseImpl(bookRepository);
  }
}
