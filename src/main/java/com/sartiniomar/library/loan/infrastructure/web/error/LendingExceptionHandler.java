package com.sartiniomar.library.loan.infrastructure.web.error;

import com.sartiniomar.library.loan.domain.bookInstance.BookAlreadyOnLoanException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.commons.infrastructure.web.error.Error;
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
  public ResponseEntity<ErrorResponse> handleBookInstanceNotFoundExceptionHandler(BookInstanceNotFoundException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(404).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), errors));
  }

  @ExceptionHandler(BookAlreadyOnLoanException.class)
  public ResponseEntity<ErrorResponse> handleBookAlreadyOnLoanExceptionHandler(BookAlreadyOnLoanException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureExceptionHandler(ObjectOptimisticLockingFailureException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }
}