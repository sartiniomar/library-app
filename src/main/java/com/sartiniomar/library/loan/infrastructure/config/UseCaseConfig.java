package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.application.usecase.PlaceHoldService;
import com.sartiniomar.library.loan.domain.loan.reserve.ReserveService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  PlaceHoldUseCase placeHoldUseCase(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      DomainEventPublisher eventPublisher,
      ReserveService service
  ) {
    return new PlaceHoldService(
        patronLoanRepository,
        bookInstanceLoanRepository,
        loanRepository,
        eventPublisher,
        service
    );
  }
}