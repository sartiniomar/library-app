package com.sartiniomar.library.lending.infrastructure.web.error;

import com.sartiniomar.library.lending.infrastructure.web.response.ErrorResponse;
import com.sartiniomar.library.lending.model.book.BookNotFoundException;
import com.sartiniomar.library.lending.model.hold.BookAlreadyOnHoldException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

  @ExceptionHandler(BookAlreadyOnHoldException.class)
  public ResponseEntity<ErrorResponse> handleBookAlreadyOnHold(BookAlreadyOnHoldException ex) {

    Map<String, String> errors = Map.of(
        "book", ex.getMessage()
    );

    return ResponseEntity.status(409).body(
        new ErrorResponse(
            "BOOK_ALREADY_ON_HOLD",
            errors,
            Instant.now()
        )
    );
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {

    Map<String, String> errors = Map.of(
        "book", "Book already on hold"
    );

    return ResponseEntity.status(409).body(
        new ErrorResponse(
            "CONCURRENT_MODIFICATION",
            errors,
            Instant.now()
        )
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

    Map<String, String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            DefaultMessageSourceResolvable::getDefaultMessage,
            (existing, replacement) -> existing
        ));

    return ResponseEntity.badRequest().body(
        new ErrorResponse(
            "VALIDATION_ERROR",
            errors,
            Instant.now()
        )
    );
  }
}