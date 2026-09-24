package com.sartiniomar.library.loan.infrastructure.scheduler;

import com.sartiniomar.library.loan.application.port.in.MarkOverdueReservesAsCancelledUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveDelayScheduler {

  private final MarkOverdueReservesAsCancelledUseCase useCase;

  @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
  public void checkOverdueReserves() {
    log.info("Starting overdue reserves check");
    useCase.execute();
    log.info("Overdue reserves check finished");
  }

}
