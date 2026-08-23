package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
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
    BookInstance existing =  BookInstance.circulating(UUID.randomUUID());

    when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));
    when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

    UpdateBookInstanceCommand command = new UpdateBookInstanceCommand(existing.getId(), BookType.CIRCULATING, true);

    BookInstance result = useCase.execute(command);

    assertEquals(existing.getId(), result.getId());
    assertEquals(existing.getBookId(), result.getBookId());
    assertEquals(BookType.CIRCULATING, result.getType());
    assertTrue(result.isOnLoan());

    verify(repository, times(1)).findById(existing.getId());
    verify(repository, times(1)).save(any());
  }

  @Test
  void shouldThrowWhenBookInstanceNotFound() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    UpdateBookInstanceCommand command = new UpdateBookInstanceCommand(id, BookType.CIRCULATING, true);

    assertThrows(BookInstanceNotFoundException.class, () -> useCase.execute(command));
  }
}
