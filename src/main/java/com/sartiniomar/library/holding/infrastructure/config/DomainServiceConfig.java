package com.sartiniomar.library.holding.infrastructure.config;

import com.sartiniomar.library.holding.model.hold.PlacingOnHoldService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

  @Bean
  PlacingOnHoldService placingOnHoldService() {
    return new PlacingOnHoldService();
  }
}
