package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class DeleteBookInstanceServiceTest {

  @Mock
  private BookInstanceRepository bookInstanceRepository;

  @InjectMocks
  private DeleteBookInstanceService deleteBookInstanceService;

  @Test
  void shouldExecuteBookInstanceSuccessfully() {
    BookInstance existing = BookInstance.circulating(UUID.randomUUID());

    when(bookInstanceRepository.findById(existing.getId())).thenReturn(Optional.of(existing));

    deleteBookInstanceService.execute(existing.getId());

    verify(bookInstanceRepository, times(1)).delete(existing.getId());
  }

  @Test
  void shouldThrowWhenBookInstanceNotFound() {
    UUID id = UUID.randomUUID();

    when(bookInstanceRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(BookInstanceNotFoundException.class,
        () -> deleteBookInstanceService.execute(id));

    verify(bookInstanceRepository, never()).delete(any());
  }
}
