package com.sartiniomar.library.loan.integration.support.factory;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.support.builder.LoanTestDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.UUID;

@Component
public class LoanTestFactory {

  @Autowired
  private LoanRepository loanRepository;

  public Loan createLoan(
      UUID id,
      UUID patronId,
      UUID bookInstanceId,
      LoanStatus status,
      Instant reservedAt,
      Instant lentAt,
      Instant dueAt,
      Instant returnedAt
  ) {
    return loanRepository.save(new LoanTestDataBuilder().buildLoan(
        id,
        patronId,
        bookInstanceId,
        status,
        reservedAt,
        lentAt,
        dueAt,
        returnedAt
    ));
  }

  public Loan createLoanReserve(Patron patron, BookInstance bookInstance) {
    return loanRepository.save(new LoanTestDataBuilder().buildReserve(patron, bookInstance));
  }

  public Loan createLoanLent(Patron patron, BookInstance bookInstance) {
    return loanRepository.save(new LoanTestDataBuilder().buildCheckout(patron, bookInstance));
  }
}
