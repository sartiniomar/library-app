package com.sartiniomar.library.patron.infrastructure.web.error;

import com.sartiniomar.library.commons.infrastructure.web.response.ErrorResponse;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class PatronExceptionHandler {

  @ExceptionHandler(PatronNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePatronNotFound(PatronNotFoundException ex) {
    Map<String, String> errors = Map.of(
        "patronId", ex.getMessage()
    );

    return ResponseEntity.status(404).body(
        new ErrorResponse(
            "PATRON_NOT_FOUND",
            errors,
            Instant.now()
        )
    );
  }

  @ExceptionHandler(PatronAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handlePatronAlreadyExists(PatronAlreadyExistsException ex) {
    Map<String, String> errors = Map.of(
        "email", ex.getMessage()
    );

    return ResponseEntity.status(409).body(
        new ErrorResponse(
            "PATRON_ALREADY_EXISTS",
            errors,
            Instant.now()
        )
    );
  }

}
