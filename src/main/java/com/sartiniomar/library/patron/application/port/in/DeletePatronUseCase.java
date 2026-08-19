package com.sartiniomar.library.patron.application.port.in;

import java.util.UUID;

public interface DeletePatronUseCase {
  void execute(UUID id);
}
