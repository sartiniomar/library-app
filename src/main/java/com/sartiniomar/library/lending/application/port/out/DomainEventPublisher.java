package com.sartiniomar.library.lending.application.port.out;

public interface DomainEventPublisher {
  void publish(Object event);
}
