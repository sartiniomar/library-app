package com.sartiniomar.library.lending.infrastructure.events;

import com.sartiniomar.library.lending.model.hold.BookPlacedOnHoldEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HoldEventListener {

  @EventListener
  public void handle(BookPlacedOnHoldEvent event) {
    System.out.println("📚 Book placed on hold: " + event.getBookId());
  }

}
