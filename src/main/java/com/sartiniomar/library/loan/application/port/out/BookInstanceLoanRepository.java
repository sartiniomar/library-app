package com.sartiniomar.library.loan.application.port.out;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceLoanRepository {
  Optional<BookInstance> findById(UUID bookInstanceId);
}
