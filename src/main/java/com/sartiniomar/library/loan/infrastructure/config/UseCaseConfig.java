package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.application.port.in.PlaceHoldUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.application.usecase.ReserveService;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  PlaceHoldUseCase reserveUseCase(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      DomainEventPublisher eventPublisher,
      ReserveServiceDomain service
  ) {
    return new ReserveService(
        patronLoanRepository,
        bookInstanceLoanRepository,
        loanRepository,
        eventPublisher,
        service
    );
  }
}