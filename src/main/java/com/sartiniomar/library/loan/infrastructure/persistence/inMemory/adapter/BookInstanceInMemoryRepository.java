package com.sartiniomar.library.loan.infrastructure.persistence.inMemory.adapter;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookInstanceInMemoryRepository implements BookInstanceLoanRepository {

  private final Map<UUID, BookInstance> catalogStorage = new ConcurrentHashMap<>();

  @Override
  public Optional<BookInstance> findById(UUID bookInstanceId) {
    return Optional.ofNullable(catalogStorage.getOrDefault(bookInstanceId,
        new BookInstance(bookInstanceId, UUID.randomUUID(), BookType.CIRCULATING, BookInstanceStatus.AVAILABLE)));
  }
}
