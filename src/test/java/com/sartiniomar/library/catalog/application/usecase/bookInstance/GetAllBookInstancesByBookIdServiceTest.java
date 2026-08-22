package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.application.port.out.BookRepository;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllBookInstancesByBookIdServiceTest {

  @Mock
  private BookInstanceRepository bookInstanceRepository;

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private GetAllBookInstancesByBookIdService useCase;

  @Test
  void shouldExecuteSuccessfully() {
    Book book = Book.create("Title", "Author", "123");
    UUID bookId = book.getId();

    BookInstance existing0 = BookInstance.circulating(bookId);
    BookInstance existing1 = BookInstance.restricted(bookId);
    existing1.setOnHold(true);

    when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(book));

    when(bookInstanceRepository.findAllByBookId(bookId)).thenReturn(List.of(existing0, existing1));

    List<BookInstance> result = useCase.execute(bookId);

    assertEquals(2, result.size());
    assertTrue(result.contains(existing0));
    assertTrue(result.contains(existing1));
    assertEquals(bookId, result.get(0).getBookId());
    assertEquals(bookId, result.get(1).getBookId());
    assertEquals(BookType.CIRCULATING, result.get(0).getType());
    assertEquals(BookType.RESTRICTED, result.get(1).getType());
    assertFalse(result.get(0).isOnHold());
    assertTrue(result.get(1).isOnHold());

    verify(bookInstanceRepository, times(1)).findAllByBookId(bookId);
  }
}
