package com.sartiniomar.library.catalog.infrastructure.web.error;

import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.commons.infrastructure.web.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class CatalogExceptionHandler {

  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
    Map<String, String> errors = Map.of(
        "bookId", ex.getMessage()
    );

    return ResponseEntity.status(404).body(
        new ErrorResponse(
            "BOOK_NOT_FOUND",
            errors,
            Instant.now()
        )
    );
  }

  @ExceptionHandler(BookAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleBookAlreadyExists(BookAlreadyExistsException ex) {
    Map<String, String> errors = Map.of(
        "isbn", ex.getMessage()
    );

    return ResponseEntity.status(409).body(
        new ErrorResponse(
            "BOOK_ALREADY_EXISTS",
            errors,
            Instant.now()
        )
    );
  }

}
