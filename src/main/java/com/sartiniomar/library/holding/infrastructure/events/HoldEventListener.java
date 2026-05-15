package com.sartiniomar.library.holding.infrastructure.events;

import com.sartiniomar.library.holding.model.hold.BookPlacedOnHoldEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HoldEventListener {

  @EventListener
  public void handle(BookPlacedOnHoldEvent event) {
    System.out.println("📚 Book placed on hold: " + event.getBookId());
  }

}
