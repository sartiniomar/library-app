package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookInstanceNotFoundException;
import com.sartiniomar.library.lending.domain.book.BookType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    UUID id = UUID.randomUUID();

    BookInstance existing = new BookInstance(id, UUID.randomUUID(), BookType.CIRCULATING, true);

    when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));

    BookInstance result = useCase.execute(existing.getId());

    assertEquals(id, result.getId());
    assertEquals(BookType.CIRCULATING, result.getType());
    assertEquals(existing.getBookId(), result.getBookId());
    assertTrue(result.isOnHold());

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
