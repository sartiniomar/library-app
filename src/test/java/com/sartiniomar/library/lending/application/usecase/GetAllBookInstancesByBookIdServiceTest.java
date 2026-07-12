package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookType;
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
  private BookInstanceRepository repository;

  @InjectMocks
  private GetAllBookInstancesByBookIdService useCase;

  @Test
  void shouldExecuteSuccessfully() {

    UUID id0 = UUID.randomUUID();
    UUID id1 = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();

    BookInstance existing0 = new BookInstance(id0, bookId, BookType.CIRCULATING, false);
    BookInstance existing1 = new BookInstance(id1, bookId, BookType.RESTRICTED, true);

    when(repository.findAllByBookId(bookId)).thenReturn(List.of(existing0, existing1));

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

    verify(repository, times(1)).findAllByBookId(bookId);
  }
}
