package com.sartiniomar.library.patron.infrastructure.web.error;

import com.sartiniomar.library.commons.infrastructure.web.error.Error;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.patron.domain.patron.PatronAlreadyExistsException;
import com.sartiniomar.library.patron.domain.patron.PatronNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class PatronExceptionHandler {

  @ExceptionHandler(PatronNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePatronNotFound(PatronNotFoundException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(404).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), errors));
  }

  @ExceptionHandler(PatronAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handlePatronAlreadyExists(PatronAlreadyExistsException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }
}
