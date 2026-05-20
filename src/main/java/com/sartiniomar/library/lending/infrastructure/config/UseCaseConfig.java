package com.sartiniomar.library.lending.infrastructure.config;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.lending.application.port.out.HoldRepository;
import com.sartiniomar.library.lending.application.port.out.PatronLendingRepository;
import com.sartiniomar.library.lending.application.usecase.PlaceHoldService;
import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

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