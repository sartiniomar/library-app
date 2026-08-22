package com.sartiniomar.library.patron.application.port.in;

import com.sartiniomar.library.patron.domain.patron.Patron;

public interface CreateRegularPatronUseCase {
  Patron execute(CreatePatronCommand command);
}
