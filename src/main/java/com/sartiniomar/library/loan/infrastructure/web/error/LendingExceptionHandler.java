package com.sartiniomar.library.loan.infrastructure.web.error;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.commons.infrastructure.web.error.Error;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class
LendingExceptionHandler {

  @ExceptionHandler(BookInstanceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookInstanceNotFound(BookInstanceNotFoundException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(404).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), errors));
  }

  @ExceptionHandler(BookType.BookAlreadyOnHoldException.class)
  public ResponseEntity<ErrorResponse> handleBookAlreadyOnHold(BookType.BookAlreadyOnHoldException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }
}