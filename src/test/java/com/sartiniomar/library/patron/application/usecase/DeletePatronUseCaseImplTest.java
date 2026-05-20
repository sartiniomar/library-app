package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import com.sartiniomar.library.patron.domain.patron.PatronType;
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
public class DeletePatronUseCaseImplTest {

  @Mock
  private PatronRepository repository;

  @InjectMocks
  private DeletePatronUseCaseImpl useCase;

  @Test
  void shouldExecutePatronSuccessfully() {
    UUID id = UUID.randomUUID();
    Patron existing = new Patron(id, PatronType.REGULAR, "Author", "123");

    when(repository.findById(id)).thenReturn(Optional.of(existing));

    useCase.execute(id);

    verify(repository, times(1)).delete(id);
  }

  @Test
  void shouldThrowWhenPatronNotFound() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(PatronNotFoundException.class,
        () -> useCase.execute(id));

    verify(repository, never()).delete(any());
  }
}
