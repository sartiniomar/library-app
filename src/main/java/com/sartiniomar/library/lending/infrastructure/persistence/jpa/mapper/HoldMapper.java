package com.sartiniomar.library.lending.infrastructure.persistence.jpa.mapper;

import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.HoldEntity;
import com.sartiniomar.library.lending.domain.hold.Hold;

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