package com.sartiniomar.library.loan.application.port.in.reserve;

import com.sartiniomar.library.loan.domain.loan.Loan;

public interface ReserveUseCase {
  Loan execute(ReserveCommand command);
}
