package com.sartiniomar.library.lending.infrastructure.config;

import com.sartiniomar.library.lending.application.port.in.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.DeleteBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.lending.application.port.in.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.application.port.in.UpdateBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.lending.application.port.out.HoldRepository;
import com.sartiniomar.library.lending.application.port.out.PatronLendingRepository;
import com.sartiniomar.library.lending.application.usecase.CreateCirculatingBookInstanceService;
import com.sartiniomar.library.lending.application.usecase.CreateRestrictedBookInstanceService;
import com.sartiniomar.library.lending.application.usecase.DeleteBookInstanceService;
import com.sartiniomar.library.lending.application.usecase.GetAllBookInstancesByBookIdService;
import com.sartiniomar.library.lending.application.usecase.GetBookInstanceByIdService;
import com.sartiniomar.library.lending.application.usecase.PlaceHoldService;
import com.sartiniomar.library.lending.application.usecase.UpdateBookInstanceService;
import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  CreateCirculatingBookInstanceUseCase createCirculatingBookInstanceUseCase(
      BookInstanceRepository bookInstanceRepository
  ) {
    return new CreateCirculatingBookInstanceService(
        bookInstanceRepository
    );
  }

  @Bean
  CreateRestrictedBookInstanceUseCase createRestrictedBookInstanceUseCase(
      BookInstanceRepository bookInstanceRepository
  ) {
    return new CreateRestrictedBookInstanceService(
        bookInstanceRepository
    );
  }

  @Bean
  DeleteBookInstanceUseCase deleteBookInstanceUseCase(
      BookInstanceRepository bookInstanceRepository
  ) {
    return new DeleteBookInstanceService(
        bookInstanceRepository
    );
  }

  @Bean
  GetAllBookInstancesByBookIdUseCase getAllBookInstancesByBookIdUseCase(
      BookInstanceRepository bookInstanceRepository
  ) {
    return new GetAllBookInstancesByBookIdService(
        bookInstanceRepository
    );
  }

  @Bean
  GetBookInstanceByIdUseCase getBookInstanceByIdUseCase(
      BookInstanceRepository bookInstanceRepository
  ) {
    return new GetBookInstanceByIdService(
        bookInstanceRepository
    );
  }

  @Bean
  UpdateBookInstanceUseCase updateBookInstanceUseCase(
      BookInstanceRepository bookInstanceRepository
  ) {
    return new UpdateBookInstanceService(
        bookInstanceRepository
    );
  }

  @Bean
  PlaceHoldUseCase placeHoldUseCase(
      PatronLendingRepository patronRepository,
      BookInstanceRepository bookInstanceRepository,
      HoldRepository holdRepository,
      DomainEventPublisher eventPublisher,
      PlacingOnHoldService service
  ) {
    return new PlaceHoldService(
        patronRepository,
        bookInstanceRepository,
        holdRepository,
        eventPublisher,
        service
    );
  }
}