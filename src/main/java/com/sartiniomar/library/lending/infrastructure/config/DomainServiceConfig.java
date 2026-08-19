package com.sartiniomar.library.lending.infrastructure.config;

import com.sartiniomar.library.lending.domain.hold.PlacingOnHoldService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

  @Bean
  PlacingOnHoldService placingOnHoldService() {
    return new PlacingOnHoldService();
  }
}
