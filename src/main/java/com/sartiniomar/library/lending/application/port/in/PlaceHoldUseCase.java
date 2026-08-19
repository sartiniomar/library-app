package com.sartiniomar.library.lending.application.port.in;

import com.sartiniomar.library.lending.domain.hold.Hold;

public interface PlaceHoldUseCase {
  Hold execute(PlaceHoldCommand command);
}
