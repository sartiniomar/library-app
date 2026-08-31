package com.sartiniomar.library.catalog.application.usecase.book;

import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookCommand;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UpdateBookUseCaseImplTest {

  @Mock
  private BookRepository repository;

  @InjectMocks
  private UpdateBookUseCaseImpl useCase;

  @Test
  void shouldExecuteBookSuccessfully() {
    Book book = new BookTestDataBuilder().buildDefault();

    when(repository.findById(book.getId())).thenReturn(Optional.of(book));
    when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

    UpdateBookCommand command = new UpdateBookCommand(book.getId(), "New", null, null);

    Book result = useCase.execute(command);

    assertEquals("New", result.getTitle());
    assertEquals("Author", result.getAuthor());
    assertEquals("123", result.getIsbn());

    verify(repository, times(1)).findById(book.getId());
    verify(repository, times(1)).save(any());
  }

  @Test
  void shouldThrowWhenBookNotFound() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    UpdateBookCommand command = new UpdateBookCommand(id, "New", null, null);

    assertThrows(BookNotFoundException.class, () -> useCase.execute(command));
  }

  @Test
  void shouldThrowWhenIsbnAlreadyExists() {
    Book book = new BookTestDataBuilder().buildDefault();
    Book bookWithSameIsbn = new BookTestDataBuilder().build("Marea", "Another Author", "999");

    when(repository.findById(book.getId())).thenReturn(Optional.of(book));
    when(repository.findByIsbn("999")).thenReturn(Optional.of(bookWithSameIsbn));

    UpdateBookCommand command = new UpdateBookCommand(book.getId(), null, null, "999");

    assertThrows(BookAlreadyExistsException.class, () -> useCase.execute(command));
  }
}
