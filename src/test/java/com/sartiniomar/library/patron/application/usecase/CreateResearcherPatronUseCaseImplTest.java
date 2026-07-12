package com.sartiniomar.library.patron.application.usecase;

import com.sartiniomar.library.patron.application.port.in.CreatePatronCommand;
import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateResearcherPatronUseCaseImplTest {
  @Mock
  private PatronRepository repository;

  @InjectMocks
  private CreateResearcherPatronUseCaseImpl useCase;

  @Test
  void shouldExecuteSuccessfully() {
    when(repository.existsByEmail("email@example.com")).thenReturn(false);
    when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

    CreatePatronCommand command = new CreatePatronCommand(
        "Name", "email@example.com"
    );

    Patron result = useCase.execute(command);

    assertEquals(PatronType.RESEARCHER, result.getType());
    assertEquals("Name", result.getName());
    assertEquals("email@example.com", result.getEmail());

    verify(repository, times(1)).existsByEmail(any());
    verify(repository, times(1)).save(any());
  }

  @Test
  void shouldThrowWhenEmailAlreadyExists() {
    when(repository.existsByEmail("email@example.com")).thenReturn(true);

    CreatePatronCommand command = new CreatePatronCommand(
        "Name", "email@example.com"
    );

    assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
  }
}
