package com.sartiniomar.library.commons.infrastructure.web.error;

import com.sartiniomar.library.commons.infrastructure.web.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

    Map<String, String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            fe -> fe.getDefaultMessage() == null ? "" : fe.getDefaultMessage(),
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
