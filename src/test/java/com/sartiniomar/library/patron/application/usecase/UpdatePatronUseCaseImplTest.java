package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.in.UpdatePatronCommand;
import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatePatronUseCaseImplTest {

  @Mock
  private PatronRepository repository;

  @InjectMocks
  private UpdatePatronUseCaseImpl useCase;

  @Test
  void shouldExecuteSuccessfully() {

    UUID id = UUID.randomUUID();

    Patron existing = new Patron(id, PatronType.REGULAR, "Old Name", "old@example.com");

    when(repository.findById(id)).thenReturn(Optional.of(existing));
    when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

    UpdatePatronCommand command = new UpdatePatronCommand(
        id, PatronType.RESEARCHER, "New Name", null
    );

    Patron result = useCase.execute(command);

    assertEquals(PatronType.RESEARCHER, result.getType());
    assertEquals("New Name", result.getName());
    assertEquals("old@example.com", result.getEmail());

    verify(repository, times(1)).findById(id);
    verify(repository, times(1)).save(any());
  }

  @Test
  void shouldThrowWhenPatronNotFound() {

    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    UpdatePatronCommand command = new UpdatePatronCommand(
        id, PatronType.RESEARCHER, "New Name", null
    );

    assertThrows(PatronNotFoundException.class,
        () -> useCase.execute(command));
  }

  @Test
  void shouldThrowWhenEmailAlreadyExists() {

    UUID id = UUID.randomUUID();

    Patron existing = new Patron(id, PatronType.REGULAR, "Old Name", "old@example.com");

    when(repository.findById(id)).thenReturn(Optional.of(existing));
    when(repository.findByEmail("new@example.com"))
        .thenReturn(Optional.of(existing));

    UpdatePatronCommand command = new UpdatePatronCommand(
        id, PatronType.RESEARCHER, "New Name", "new@example.com"
    );

    assertThrows(PatronAlreadyExistsException.class,
        () -> useCase.execute(command));
  }
}
