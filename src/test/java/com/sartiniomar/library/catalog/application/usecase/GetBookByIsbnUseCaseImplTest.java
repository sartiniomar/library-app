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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetBookByIsbnUseCaseImplTest {

  @Mock
  private BookRepository repository;

  @InjectMocks
  private GetBookByIsbnUseCaseImpl useCase;

  @Test
  void shouldGetBookByIsbn() {
    UUID id = UUID.randomUUID();
    Book existing = new Book(id, "Title", "Author", "123");

    when(repository.findByIsbn(existing.getIsbn())).thenReturn(Optional.of(existing));

    Book result = useCase.get(existing.getIsbn());

    assertEquals("Title", result.getTitle());
    assertEquals("Author", result.getAuthor());
    assertEquals("123", result.getIsbn());

    verify(repository, times(1)).findByIsbn(existing.getIsbn());
  }

  @Test
  void shouldThrowWhenBookNotFound() {
    when(repository.findByIsbn(any())).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class,
        () -> useCase.get("inexistent Isbn"));

    verify(repository, times(1)).findByIsbn(any());
  }
}
