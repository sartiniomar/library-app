package com.sartiniomar.library.lending.application.port.in;

import java.util.UUID;

public interface GetAllBookInstancesByBookIdUseCase {
  void execute(UUID command);
}
