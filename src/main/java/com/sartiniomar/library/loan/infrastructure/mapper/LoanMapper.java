package com.sartiniomar.library.loan.infrastructure.mapper;

import com.sartiniomar.library.catalog.infrastructure.persistence.model.BookInstanceEntity;
import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import com.sartiniomar.library.loan.infrastructure.web.dto.CreateLoanRequest;
import com.sartiniomar.library.loan.infrastructure.web.dto.LoanResponse;
import com.sartiniomar.library.patron.infrastructure.persistence.model.PatronEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LoanMapper {

  LoanCommand createLoanRequestToLoanCommand(CreateLoanRequest placeHoldRequest);

  LoanResponse loanToLoanResponse(Loan loan);

  @Mapping(target = "patron", source = "patronId")
  @Mapping(target = "bookInstance", source = "bookInstanceId")
  LoanEntity toEntity(Loan loan);

  default PatronEntity mapPatron(UUID patronId) {
    if (patronId == null) {
      return null;
    }

    PatronEntity patron = new PatronEntity();
    patron.setId(patronId);
    return patron;
  }

  default BookInstanceEntity mapBookInstance(UUID bookInstanceId) {
    if (bookInstanceId == null) {
      return null;
    }

    BookInstanceEntity bookInstance = new BookInstanceEntity();
    bookInstance.setId(bookInstanceId);
    return bookInstance;
  }

  default Loan toDomain(LoanEntity entity) {
    if (entity == null) {
      return null;
    }

    return Loan.withId(
        entity.getId(),
        entity.getPatron().getId(),
        entity.getBookInstance().getId(),
        entity.getStatus(),
        entity.getReservedAt(),
        entity.getLentAt(),
        entity.getDueAt(),
        entity.getReturnedAt()
    );
  }
}