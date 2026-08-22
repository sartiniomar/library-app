package com.sartiniomar.library.loan.application.port.in;

import com.sartiniomar.library.loan.domain.loan.Loan;

public interface PlaceHoldUseCase {
  Loan execute(PlaceHoldCommand command);
}
