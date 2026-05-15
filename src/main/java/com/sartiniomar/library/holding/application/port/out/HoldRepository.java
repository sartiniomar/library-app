package com.sartiniomar.library.holding.application.port.out;

import com.sartiniomar.library.holding.model.hold.Hold;
import java.util.UUID;

public interface HoldRepository {
  Integer countByPatronId(UUID patronId);
  void save(Hold hold);
}
