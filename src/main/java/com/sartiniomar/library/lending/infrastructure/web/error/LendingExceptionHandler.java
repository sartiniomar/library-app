package com.sartiniomar.library.lending.infrastructure.web.error;

import com.sartiniomar.library.lending.domain.book.BookInstanceNotFoundException;
import com.sartiniomar.library.commons.infrastructure.web.response.ErrorResponse;
import com.sartiniomar.library.lending.domain.hold.BookAlreadyOnHoldException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class
LendingExceptionHandler {

  @ExceptionHandler(BookInstanceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookInstanceNotFound(BookInstanceNotFoundException ex) {
    Map<String, String> errors = Map.of(
        "bookInstanceId", ex.getMessage()
    );

    return ResponseEntity.status(404).body(
        new ErrorResponse(
            "BOOK_INSTANCE_NOT_FOUND",
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
}