package com.sartiniomar.library.lending.application.port.in;

import java.util.UUID;

public interface DeleteBookInstanceUseCase {
  void execute(UUID command);
}
