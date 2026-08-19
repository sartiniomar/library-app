package com.sartiniomar.library.catalog.application.port.in.bookInstance;

import java.util.UUID;

public interface DeleteBookInstanceUseCase {
  void execute(UUID command);
}
