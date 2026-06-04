package com.sartiniomar.library.catalog.infrastructure.web;

import com.sartiniomar.library.catalog.application.port.in.CreateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.CreateBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.DeleteBookUseCase;
import com.sartiniomar.library.catalog.application.port.in.GetBookByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.GetBookByIsbnUseCase;
import com.sartiniomar.library.catalog.application.port.in.UpdateBookCommand;
import com.sartiniomar.library.catalog.application.port.in.UpdateBookUseCase;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.web.request.BookRequest;
import jakarta.validation.Valid;
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
@RequestMapping("/books")
public class BookController {

  private final CreateBookUseCase createBook;
  private final UpdateBookUseCase updateBook;
  private final GetBookByIdUseCase getBookById;
  private final GetBookByIsbnUseCase getBookByIsbn;
  private final DeleteBookUseCase deleteBook;

  public BookController(CreateBookUseCase createBook, UpdateBookUseCase updateBook, GetBookByIdUseCase getBookById, GetBookByIdUseCase getBookById1, GetBookByIsbnUseCase getBookByIsbn, DeleteBookUseCase deleteBook) {
    this.createBook = createBook;
    this.updateBook = updateBook;
    this.getBookById = getBookById1;
    this.getBookByIsbn = getBookByIsbn;
    this.deleteBook = deleteBook;
  }

  @PostMapping
  public ResponseEntity<Book> create(@Valid @RequestBody BookRequest createBookRequest) {

    CreateBookCommand cmd = new CreateBookCommand(
        createBookRequest.title(),
        createBookRequest.author(),
        createBookRequest.isbn()
    );
    return ResponseEntity.ok(createBook.create(cmd));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Book> update(@PathVariable UUID id,
      @RequestBody BookRequest updateBookRequest) {

    UpdateBookCommand cmd = new UpdateBookCommand(
        id,
        updateBookRequest.title(),
        updateBookRequest.author(),
        updateBookRequest.isbn()
    );
    return ResponseEntity.ok(updateBook.update(cmd));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Book> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(getBookById.get(id));
  }

  @GetMapping
  public ResponseEntity<Book> getByIsbn(@RequestParam String isbn) {
    return ResponseEntity.ok(getBookByIsbn.get(isbn));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    deleteBook.delete(id);
    return ResponseEntity.noContent().build();
  }
}
