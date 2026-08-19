package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.catalog.support.builder.BookTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

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
  void shouldExecuteBookByIsbn() {
    Book book = new BookTestDataBuilder().buildDefault();
    when(repository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

    Book result = useCase.execute(book.getIsbn());

    assertEquals("Title", result.getTitle());
    assertEquals("Author", result.getAuthor());
    assertEquals("123", result.getIsbn());

    verify(repository, times(1)).findByIsbn(book.getIsbn());
  }

  @Test
  void shouldThrowWhenBookNotFound() {
    when(repository.findByIsbn(any())).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> useCase.execute("inexistent Isbn"));

    verify(repository, times(1)).findByIsbn(any());
  }
}
