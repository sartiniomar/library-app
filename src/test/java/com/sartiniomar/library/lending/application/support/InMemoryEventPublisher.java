package com.sartiniomar.library.lending.application.support;

import com.sartiniomar.library.lending.application.port.out.DomainEventPublisher;
import java.util.ArrayList;
import java.util.List;

public class InMemoryEventPublisher implements DomainEventPublisher {

  public List<Object> events = new ArrayList<>();

  @Override
  public void publish(Object event) {
    events.add(event);
  }
}
