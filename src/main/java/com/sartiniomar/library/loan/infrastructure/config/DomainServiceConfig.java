package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;

@Configuration
public class DomainServiceConfig {

  @Bean
  ReserveServiceDomain reserveService() {
    return new ReserveServiceDomain(Clock.systemDefaultZone());
  }
}
