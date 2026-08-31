package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.service.LoanLimitChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

  @Bean
  LoanLimitChecker validationsUtil(LoanRepository loanRepository) {
    return new LoanLimitChecker(loanRepository);
  }
}
