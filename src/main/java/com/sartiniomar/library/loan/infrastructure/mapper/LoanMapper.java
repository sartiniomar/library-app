package com.sartiniomar.library.loan.infrastructure.mapper;

import com.sartiniomar.library.loan.application.port.in.reserve.ReserveCommand;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import com.sartiniomar.library.loan.infrastructure.web.dto.HoldResponse;
import com.sartiniomar.library.loan.infrastructure.web.dto.PlaceHoldRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {

  ReserveCommand placeHoldRequestToPlaceHoldCommand(PlaceHoldRequest placeHoldRequest);

  HoldResponse holdToHoldResponse(Loan hold);

  Loan toDomain(LoanEntity entity);

  LoanEntity toEntity(Loan hold);
}
