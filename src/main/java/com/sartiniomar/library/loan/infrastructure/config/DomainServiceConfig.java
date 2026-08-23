package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.domain.loan.ReserveService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

  @Bean
  ReserveService placingOnHoldService() {
    return new ReserveService();
  }
}
