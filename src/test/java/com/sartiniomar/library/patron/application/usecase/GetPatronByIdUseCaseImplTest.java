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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GetPatronByIdUseCaseImplTest {

  @Mock
  private PatronRepository repository;

  @InjectMocks
  private GetPatronByIdUseCaseImpl useCase;

  @Test
  void shouldGetPatronById() {
    UUID id = UUID.randomUUID();
    Patron existing = new Patron(id, PatronType.REGULAR, "Name", "email@example.com");

    when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));

    Patron result = useCase.execute(existing.getId());

    assertEquals(PatronType.REGULAR, result.getType());
    assertEquals("Name", result.getName());
    assertEquals("email@example.com", result.getEmail());

    verify(repository, times(1)).findById(existing.getId());
  }

  @Test
  void shouldThrowWhenPatronNotFound() {
    UUID inexistentId = UUID.randomUUID();

    when(repository.findById(any())).thenReturn(Optional.empty());

    assertThrows(PatronNotFoundException.class,
        () -> useCase.execute(inexistentId));

    verify(repository, times(1)).findById(any());
  }
}
