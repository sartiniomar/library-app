package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.catalog.support.builder.BookTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCirculatingBookInstanceServiceTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private BookInstanceRepository repository;

  @InjectMocks
  private CreateCirculatingBookInstanceService useCase;

  @Test
  void shouldExecuteSuccessfully() {
    Book book = new BookTestDataBuilder().build("Title", "Author", "123");

    CreateBookInstanceCommand command = new CreateBookInstanceCommand(book.getId());

    when(bookRepository.findById(book.getId()))
        .thenReturn(Optional.of(book));

    BookInstance result = useCase.execute(command);

    assertNotNull(result.getId());
    assertEquals(book.getId(), result.getBookId());
    assertEquals(BookType.CIRCULATING, result.getType());
    assertFalse(result.isOnHold());

    verify(bookRepository, times(1)).findById(book.getId());
    verify(repository, times(1)).save(any());
  }
}
