package com.sartiniomar.library.loan.application.port.in.cancel;

import com.sartiniomar.library.loan.domain.loan.Loan;

public interface CancelUseCase {
  Loan execute(CancelCommand command);
}
