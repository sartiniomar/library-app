package com.sartiniomar.library.lending.application.port.in;

public interface CreateRestrictedBookInstanceUseCase {
  void execute(CreateBookInstanceCommand command);
}
