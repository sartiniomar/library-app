package com.sartiniomar.library.catalog.infrastructure.config;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.DeleteBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.application.usecase.bookInstance.CreateCirculatingBookInstanceService;
import com.sartiniomar.library.catalog.application.usecase.bookInstance.CreateRestrictedBookInstanceService;
import com.sartiniomar.library.catalog.application.usecase.bookInstance.DeleteBookInstanceService;
import com.sartiniomar.library.catalog.application.usecase.bookInstance.GetAllBookInstancesByBookIdService;
import com.sartiniomar.library.catalog.application.usecase.bookInstance.GetBookInstanceByIdService;
import com.sartiniomar.library.catalog.application.usecase.bookInstance.UpdateBookInstanceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookInstanceConfig {

  @Bean
  CreateCirculatingBookInstanceUseCase createCirculatingBookInstanceUseCase(BookInstanceRepository bookInstanceRepository, BookRepository bookRepository) {
    return new CreateCirculatingBookInstanceService(
        bookInstanceRepository,
        bookRepository
    );
  }

  @Bean
  CreateRestrictedBookInstanceUseCase createRestrictedBookInstanceUseCase(BookInstanceRepository bookInstanceRepository, BookRepository bookRepository) {
    return new CreateRestrictedBookInstanceService(
        bookInstanceRepository,
        bookRepository);
  }

  @Bean
  DeleteBookInstanceUseCase deleteBookInstanceUseCase(BookInstanceRepository bookInstanceRepository) {
    return new DeleteBookInstanceService(
        bookInstanceRepository
    );
  }

  @Bean
  GetAllBookInstancesByBookIdUseCase getAllBookInstancesByBookIdUseCase(BookInstanceRepository bookInstanceRepository, BookRepository bookRepository) {
    return new GetAllBookInstancesByBookIdService(
        bookInstanceRepository,
        bookRepository
    );
  }

  @Bean
  GetBookInstanceByIdUseCase getBookInstanceByIdUseCase(BookInstanceRepository bookInstanceRepository) {
    return new GetBookInstanceByIdService(
        bookInstanceRepository
    );
  }

  @Bean
  UpdateBookInstanceUseCase updateBookInstanceUseCase(BookInstanceRepository bookInstanceRepository) {
    return new UpdateBookInstanceService(
        bookInstanceRepository
    );
  }

}
