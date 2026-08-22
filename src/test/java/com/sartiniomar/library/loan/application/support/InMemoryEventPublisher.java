package com.sartiniomar.library.loan.application.support;

import com.sartiniomar.library.loan.application.port.out.DomainEventPublisher;
import java.util.ArrayList;
import java.util.List;

public class InMemoryEventPublisher implements DomainEventPublisher {

  public List<Object> events = new ArrayList<>();

  @Override
  public void publish(Object event) {
    events.add(event);
  }
}
