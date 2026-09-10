package com.sartiniomar.library.loan.integration.support.factory;

import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import com.sartiniomar.library.loan.support.builder.LoanTestDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatronLoanTestFactory {

  @Autowired
  private PatronLoanRepository patronLoanRepository;

  public Patron createDefaultPatron(PatronType patronType) {
    return patronLoanRepository.save(new LoanTestDataBuilder().buildDefaultPatron(patronType));
  }
}
