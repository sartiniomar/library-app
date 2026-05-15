package com.sartiniomar.library.holding.infrastructure.events;

import com.sartiniomar.library.holding.application.port.out.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

public class SpringDomainEventPublisher implements DomainEventPublisher {

  private final ApplicationEventPublisher publisher;

  public SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(Object event) {
    publisher.publishEvent(event);
  }
}
