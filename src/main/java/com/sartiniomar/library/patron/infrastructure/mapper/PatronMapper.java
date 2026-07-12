package com.sartiniomar.library.patron.infrastructure.mapper;

import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.in.UpdatePatronCommand;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.infrastructure.persistence.jpa.model.PatronEntity;
import com.sartiniomar.library.patron.infrastructure.web.dto.CreatePatronRequest;
import com.sartiniomar.library.patron.infrastructure.web.dto.PatronResponse;
import com.sartiniomar.library.patron.infrastructure.web.dto.UpdatePatronRequest;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PatronMapper {

  CreatePatronCommand createPatronRequestToCreatePatronCommand(CreatePatronRequest createPatronRequest);

  PatronResponse patronToPatronResponse(Patron patron);

  UpdatePatronCommand updatePatronRequestToUpdatePatronCommand(UpdatePatronRequest updatePatronRequest, UUID id);

  Patron toDomain(PatronEntity entity);

  PatronEntity toEntity(Patron patron);
}
