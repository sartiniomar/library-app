package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.CreateBookInstanceCommand;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCirculatingBookInstanceServiceTest {

  @Mock
  private BookInstanceRepository repository;

  @InjectMocks
  private CreateCirculatingBookInstanceService useCase;

  @Test
  void shouldExecuteSuccessfully() {
    CreateBookInstanceCommand command = new CreateBookInstanceCommand(
        UUID.fromString("c16c86fc-c91e-47ff-b0a6-ec29359a7590")
    );

    BookInstance result = useCase.execute(command);

    assertNotNull(result.getId());
    assertEquals(UUID.fromString("c16c86fc-c91e-47ff-b0a6-ec29359a7590"), result.getBookId());
    assertEquals(BookType.CIRCULATING, result.getType());
    assertFalse(result.isOnHold());

    verify(repository, times(1)).save(any());
  }
}
