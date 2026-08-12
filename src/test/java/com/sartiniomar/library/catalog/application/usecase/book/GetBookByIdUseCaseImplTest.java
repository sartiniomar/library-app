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
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetBookByIdUseCaseImplTest {

  @Mock
  private BookRepository repository;

  @InjectMocks
  private GetBookByIdUseCaseImpl useCase;

  @Test
  void shouldExecuteBookById() {
    Book book = new BookTestDataBuilder().buildDefault();

    when(repository.findById(book.getId())).thenReturn(Optional.of(book));

    Book result = useCase.execute(book.getId());

    assertEquals("Title", result.getTitle());
    assertEquals("Author", result.getAuthor());
    assertEquals("123", result.getIsbn());

    verify(repository, times(1)).findById(book.getId());
  }

  @Test
  void shouldThrowWhenBookNotFound() {
    UUID inexistentId = UUID.randomUUID();

    when(repository.findById(any())).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> useCase.execute(inexistentId));

    verify(repository, times(1)).findById(any());
  }
}
