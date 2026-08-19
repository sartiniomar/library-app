package com.sartiniomar.library.lending.application.port.out;

import com.sartiniomar.library.lending.domain.bookInstance.BookInstance;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceLendingRepository {
  Optional<BookInstance> findById(UUID bookInstanceId);
}
