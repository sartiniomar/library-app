package com.sartiniomar.library.lending.infrastructure.mapper;

import com.sartiniomar.library.lending.application.port.in.PlaceHoldCommand;
import com.sartiniomar.library.lending.domain.hold.Hold;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.HoldEntity;
import com.sartiniomar.library.lending.infrastructure.web.dto.HoldResponse;
import com.sartiniomar.library.lending.infrastructure.web.dto.PlaceHoldRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HoldMapper {

  PlaceHoldCommand placeHoldRequestToPlaceHoldCommand(PlaceHoldRequest placeHoldRequest);

  HoldResponse holdToHoldResponse(Hold hold);

  Hold toDomain(HoldEntity entity);

  HoldEntity toEntity(Hold hold);
}
