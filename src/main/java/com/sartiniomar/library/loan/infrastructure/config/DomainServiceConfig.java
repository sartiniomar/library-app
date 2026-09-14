package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.domain.loan.service.CancelServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutReserveServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.ReturnServiceDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;

@Configuration
public class DomainServiceConfig {

  @Bean
  ReserveServiceDomain reserveService() {
    return new ReserveServiceDomain(Clock.systemUTC());
  }

  @Bean
  CancelServiceDomain cancelService() {
    return new CancelServiceDomain();
  }

  @Bean
  CheckoutServiceDomain checkoutService() {
    return new CheckoutServiceDomain(Clock.systemUTC());
  }

  @Bean
  CheckoutReserveServiceDomain checkoutReserveServiceDomain() {
    return new CheckoutReserveServiceDomain(Clock.systemUTC());
  }

  @Bean
  ReturnServiceDomain returnServiceDomain() {
    return new ReturnServiceDomain();
  }
}
