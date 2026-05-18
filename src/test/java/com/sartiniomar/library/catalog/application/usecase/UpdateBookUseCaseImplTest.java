package com.sartiniomar.library.catalog.application.usecase;

import com.sartiniomar.library.catalog.application.port.in.UpdateBookCommand;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class UpdateBookUseCaseImplTest {

  @Mock
  private BookRepository repository;

  @InjectMocks
  private UpdateBookUseCaseImpl useCase;

  @Test
  void shouldUpdateBookSuccessfully() {

    UUID id = UUID.randomUUID();

    Book existing = Book.create("Old", "Author", "123");

    when(repository.findById(id)).thenReturn(Optional.of(existing));
    when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

    UpdateBookCommand command = new UpdateBookCommand(
        id, "New", null, null
    );

    Book result = useCase.update(command);

    assertEquals("New", result.getTitle());
    assertEquals("Author", result.getAuthor());
    assertEquals("123", result.getIsbn());

    verify(repository, times(1)).findById(id);
    verify(repository, times(1)).save(any());
  }

  @Test
  void shouldThrowWhenBookNotFound() {

    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    UpdateBookCommand command = new UpdateBookCommand(
        id, "New", null, null
    );

    assertThrows(BookNotFoundException.class,
        () -> useCase.update(command));
  }

  @Test
  void shouldThrowWhenIsbnAlreadyExists() {

    UUID id = UUID.randomUUID();

    Book existing = Book.create("Old", "Author", "123");

    when(repository.findById(id)).thenReturn(Optional.of(existing));
    when(repository.findByIsbn("999"))
        .thenReturn(Optional.of(existing));

    UpdateBookCommand command = new UpdateBookCommand(
        id, null, null, "999"
    );

    assertThrows(BookAlreadyExistsException.class,
        () -> useCase.update(command));
  }
}
