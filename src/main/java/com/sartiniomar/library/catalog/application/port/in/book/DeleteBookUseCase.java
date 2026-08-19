package com.sartiniomar.library.catalog.application.port.in.book;

import java.util.UUID;

public interface DeleteBookUseCase {
  void execute(UUID id);
}
