package com.sartiniomar.library.loan.application.port.in.checkout;

import com.sartiniomar.library.loan.domain.loan.Loan;

public interface CheckoutUseCase {
  Loan execute(CheckoutCommand command);
}
