package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.LibraryApplicationTests;
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
    UUID id = UUID.randomUUID();
    BookInstance existing = new BookInstance(id, UUID.randomUUID(), BookType.CIRCULATING, false);

    when(bookInstanceRepository.findById(id)).thenReturn(Optional.of(existing));

    deleteBookInstanceService.execute(id);

    verify(bookInstanceRepository, times(1)).delete(id);
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
