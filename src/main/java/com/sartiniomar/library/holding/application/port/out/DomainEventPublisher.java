package com.sartiniomar.library.holding.application.port.out;

public interface DomainEventPublisher {
  void publish(Object event);
}
