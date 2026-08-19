package com.sartiniomar.library.catalog.infrastructure.web.error;

import com.sartiniomar.library.catalog.domain.book.BookAlreadyExistsException;
import com.sartiniomar.library.catalog.domain.book.BookNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.commons.infrastructure.web.error.Error;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class CatalogExceptionHandler {

  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(404).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), errors));
  }

  @ExceptionHandler(BookAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleBookAlreadyExists(BookAlreadyExistsException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }

  @ExceptionHandler(BookInstanceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookInstanceNotFound(BookInstanceNotFoundException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(404).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), errors));
  }
}
