package com.sartiniomar.library.lending.application.port.in;

import java.util.UUID;

public interface GetBookInstanceByIdUseCase {
  void execute(UUID command);
}
