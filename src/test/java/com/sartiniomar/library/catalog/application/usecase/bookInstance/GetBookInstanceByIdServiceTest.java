package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GetBookInstanceByIdServiceTest {

  @Mock
  private BookInstanceRepository repository;

  @InjectMocks
  private GetBookInstanceByIdService useCase;

  @Test
  void shouldGetBookInstanceById() {
    BookInstance existing = BookInstance.circulating(UUID.randomUUID());
    existing.setOnLoan(true);

    when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));

    BookInstance result = useCase.execute(existing.getId());

    assertEquals(existing.getId(), result.getId());
    assertEquals(BookType.CIRCULATING, result.getType());
    assertEquals(existing.getBookId(), result.getBookId());
    assertEquals(BookInstanceStatus.AVAILABLE, result.getStatus());
    assertTrue(result.isOnLoan());

    verify(repository, times(1)).findById(existing.getId());
  }

  @Test
  void shouldThrowWhenBookInstanceNotFound() {
    UUID inexistentId = UUID.randomUUID();

    when(repository.findById(any())).thenReturn(Optional.empty());

    assertThrows(BookInstanceNotFoundException.class,
        () -> useCase.execute(inexistentId));

    verify(repository, times(1)).findById(any());
  }
}
