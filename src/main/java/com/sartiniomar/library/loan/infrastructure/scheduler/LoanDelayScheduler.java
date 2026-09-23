package com.sartiniomar.library.loan.infrastructure.scheduler;

import com.sartiniomar.library.loan.application.port.in.MarkOverdueLoansAsDelayedUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanDelayScheduler {

  private final MarkOverdueLoansAsDelayedUseCase useCase;

  @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
  public void checkOverdueLoans() {
    log.info("Starting overdue loans check");
    useCase.execute();
    log.info("Overdue loans check finished");
  }

}
