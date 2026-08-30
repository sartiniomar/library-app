package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.application.port.in.reserve.ReserveUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.application.usecase.ReserveUseCaseImpl;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  ReserveUseCase reserveUseCase(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      ReserveServiceDomain service
  ) {
    return new ReserveUseCaseImpl(
        patronLoanRepository,
        bookInstanceLoanRepository,
        loanRepository,
        service
    );
  }
}