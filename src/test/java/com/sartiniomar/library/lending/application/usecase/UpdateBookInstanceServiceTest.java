package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.UpdateBookInstanceCommand;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBookInstanceServiceTest {

  @Mock
  private BookInstanceRepository repository;

  @InjectMocks
  private UpdateBookInstanceService useCase;

  @Test
  void shouldExecuteSuccessfully() {

    UUID id = UUID.randomUUID();

    BookInstance existing = new BookInstance(id, UUID.randomUUID(), BookType.CIRCULATING, false);

    when(repository.findById(id)).thenReturn(Optional.of(existing));
    when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

    UpdateBookInstanceCommand command = new UpdateBookInstanceCommand(
        id, BookType.CIRCULATING, true
    );

    BookInstance result = useCase.execute(command);

    assertEquals(id, result.getId());
    assertEquals(existing.getBookId(), result.getBookId());
    assertEquals(BookType.CIRCULATING, result.getType());
    assertTrue(result.isOnHold());

    verify(repository, times(1)).findById(id);
    verify(repository, times(1)).save(any());
  }

  @Test
  void shouldThrowWhenBookInstanceNotFound() {

    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    UpdateBookInstanceCommand command = new UpdateBookInstanceCommand(
        id, BookType.CIRCULATING, true
    );

    assertThrows(BookInstanceNotFoundException.class,
        () -> useCase.execute(command));
  }
}
