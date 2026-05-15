package com.sartiniomar.library.holding.infrastructure.persistence.jpa.mapper;

import com.sartiniomar.library.holding.infrastructure.persistence.jpa.model.HoldEntity;
import com.sartiniomar.library.holding.model.hold.Hold;

public class HoldMapper {

  public static Hold toDomain(HoldEntity entity) {
    return new Hold(
        entity.getPatronId(),
        entity.getBookId()
    );
  }

  public static HoldEntity toEntity(Hold hold) {
    return new HoldEntity(
        hold.getId(),
        hold.getBookInstanceId(),
        hold.getPatronId()
    );
  }
}