package com.sartiniomar.library.loan.application.port.out;

public interface DomainEventPublisher {
  void publish(Object event);
}
