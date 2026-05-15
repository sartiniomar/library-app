package com.sartiniomar.library.holding.infrastructure.config;

import com.sartiniomar.library.holding.application.port.out.DomainEventPublisher;
import com.sartiniomar.library.holding.infrastructure.events.SpringDomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

  @Bean
  DomainEventPublisher domainEventPublisher(ApplicationEventPublisher publisher) {
    return new SpringDomainEventPublisher(publisher);
  }

}
