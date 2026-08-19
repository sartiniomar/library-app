package com.sartiniomar.library.catalog.infrastructure.web;

import com.sartiniomar.library.catalog.application.port.in.book.CreateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.book.CreateBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.book.UpdateBookUseCase;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookMapper;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookRequest;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

  private final CreateBookUseCase createBook;
  private final UpdateBookUseCase updateBook;
  private final GetBookByIdUseCase getBookById;
  private final GetBookByIsbnUseCase getBookByIsbn;
  private final DeleteBookUseCase deleteBook;
  private final BookMapper bookMapper;

  @PostMapping
  public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest createBookRequest) {
    CreateBookCommand cmd = bookMapper.bookRequestToCreateBookCommand(createBookRequest);
    return ResponseEntity.ok(bookMapper.bookToBookResponse(createBook.execute(cmd)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BookResponse> update(@PathVariable UUID id, @RequestBody BookRequest updateBookRequest) {
    UpdateBookCommand cmd = bookMapper.bookRequestToUpdateBookCommand(updateBookRequest, id);
    return ResponseEntity.ok(bookMapper.bookToBookResponse(updateBook.execute(cmd)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(bookMapper.bookToBookResponse(getBookById.execute(id)));
  }

  @GetMapping
  public ResponseEntity<BookResponse> getByIsbn(@RequestParam String isbn) {
    return ResponseEntity.ok(bookMapper.bookToBookResponse(getBookByIsbn.execute(isbn)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    deleteBook.execute(id);
    return ResponseEntity.noContent().build();
  }
}
