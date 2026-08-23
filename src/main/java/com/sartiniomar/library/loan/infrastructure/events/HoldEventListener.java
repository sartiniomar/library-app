package com.sartiniomar.library.loan.infrastructure.events;

import com.sartiniomar.library.loan.domain.loan.ReserveBookEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HoldEventListener {

  @EventListener
  public void handle(ReserveBookEvent event) {
    System.out.println("📚 Book placed on hold: " + event.getBookId());
  }

}
