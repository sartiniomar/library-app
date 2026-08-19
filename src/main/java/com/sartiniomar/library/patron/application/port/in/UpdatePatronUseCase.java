package com.sartiniomar.library.patron.application.port.in;

import com.sartiniomar.library.patron.domain.patron.Patron;

public interface UpdatePatronUseCase {
  Patron execute(UpdatePatronCommand command);
}
