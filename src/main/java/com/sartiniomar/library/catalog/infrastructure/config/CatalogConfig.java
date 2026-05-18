package com.sartiniomar.library.catalog.infrastructure.config;

import com.sartiniomar.library.catalog.application.port.in.CreateBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.in.UpdateBookUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.application.usecase.CreateBookUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.DeleteBookUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.GetBookByIdByIdUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.GetBookByIsbnUseCaseImpl;
import com.sartiniomar.library.catalog.application.usecase.UpdateBookUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

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
    return new GetBookByIdByIdUseCaseImpl(bookRepository);
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
