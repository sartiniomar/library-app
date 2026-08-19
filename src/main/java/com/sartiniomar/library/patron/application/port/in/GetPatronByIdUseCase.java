package com.sartiniomar.library.patron.application.port.in;

import com.sartiniomar.library.patron.domain.patron.Patron;
import java.util.UUID;

public interface GetPatronByIdUseCase {
  Patron execute(UUID id);
}
