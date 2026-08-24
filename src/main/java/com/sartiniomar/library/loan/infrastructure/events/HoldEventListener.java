package com.sartiniomar.library.loan.infrastructure.events;

import com.sartiniomar.library.loan.domain.loan.LoanBookEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HoldEventListener {

  @EventListener
  public void handle(LoanBookEvent event) {
    System.out.println("📚 Book placed on hold: " + event.getBookId());
  }

}
