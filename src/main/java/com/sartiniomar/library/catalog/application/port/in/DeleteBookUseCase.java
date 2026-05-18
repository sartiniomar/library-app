package com.sartiniomar.library.catalog.application.port.in;

import java.util.UUID;

public interface DeleteBookUseCase {
  void delete(UUID id);
}
