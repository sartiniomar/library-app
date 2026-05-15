package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.model.hold.Hold;
import java.util.UUID;

public interface HoldRepository {
  Integer countByPatronId(UUID patronId);
  void save(Hold hold);
}
