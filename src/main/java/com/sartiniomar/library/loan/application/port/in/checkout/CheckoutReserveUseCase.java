package com.sartiniomar.library.loan.application.port.in.checkout;

import com.sartiniomar.library.loan.domain.loan.Loan;

public interface CheckoutReserveUseCase {
  Loan execute(CheckoutReserveCommand command);
}
