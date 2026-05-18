package com.sartiniomar.library.catalog.application.usecase;

import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
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

@ExtendWith(MockitoExtension.class)
public class DeleteBookUseCaseImplTest {

  @Mock
  private BookRepository repository;

  @InjectMocks
  private DeleteBookUseCaseImpl useCase;

  @Test
  void shouldDeleteBookSuccessfully() {
    UUID id = UUID.randomUUID();
    Book existing = new Book(id, "Title", "Author", "123");

    when(repository.findById(id)).thenReturn(Optional.of(existing));

    useCase.delete(id);

    verify(repository, times(1)).delete(id);
  }

  @Test
  void shouldThrowWhenBookNotFound() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class,
        () -> useCase.delete(id));

    verify(repository, never()).delete(any());
  }
}
