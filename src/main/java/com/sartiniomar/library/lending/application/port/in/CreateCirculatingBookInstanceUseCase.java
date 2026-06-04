package com.sartiniomar.library.lending.application.port.in;

public interface CreateCirculatingBookInstanceUseCase {
  void execute(CreateBookInstanceCommand command);
}