package com.sartiniomar.library.catalog.application.usecase;

import com.sartiniomar.library.catalog.application.port.in.CreateBookCommand;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateBookUseCaseImplTest {

  @Mock
  private BookRepository repository;

  @InjectMocks
  private CreateBookUseCaseImpl useCase;

  @Test
  void shouldCreateBookSuccessfully() {
    when(repository.existsByIsbn("123")).thenReturn(false);
    when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

    CreateBookCommand command = new CreateBookCommand(
        "Title", "Author", "123"
    );

    Book result = useCase.create(command);

    assertEquals("Title", result.getTitle());
    assertEquals("Author", result.getAuthor());
    assertEquals("123", result.getIsbn());

    verify(repository, times(1)).existsByIsbn(any());
    verify(repository, times(1)).save(any());
  }

  @Test
  void shouldThrowWhenIsbnAlreadyExists() {
    when(repository.existsByIsbn("123")).thenReturn(true);

    CreateBookCommand command = new CreateBookCommand(
        "Title", "Author", "123"
    );

    assertThrows(BookAlreadyExistsException.class,
        () -> useCase.create(command));
  }
}
