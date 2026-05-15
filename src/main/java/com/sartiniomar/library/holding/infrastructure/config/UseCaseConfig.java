package com.sartiniomar.library.holding.infrastructure.config;

import com.sartiniomar.library.holding.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.holding.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.holding.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.holding.application.port.out.HoldRepository;
import com.sartiniomar.library.holding.application.port.out.PatronRepository;
import com.sartiniomar.library.holding.application.usecase.PlaceHoldService;
import com.sartiniomar.library.holding.model.hold.PlacingOnHoldService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  PlaceHoldUseCase placeHoldUseCase(
      PatronRepository patronRepository,
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