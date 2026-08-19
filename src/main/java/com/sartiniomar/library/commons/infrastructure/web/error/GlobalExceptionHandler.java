package com.sartiniomar.library.commons.infrastructure.web.error;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
    List<Error> errorList = exception
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .map(Error::new)
        .collect(Collectors.toList());
    return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorList);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse methodArgumentTypeMismatchExceptionHandler(
      MethodArgumentTypeMismatchException exception) {

    String message = String.format(
        "Parameter '%s' with value '%s' could not be converted to type %s",
        exception.getName(),
        exception.getValue(),
        exception.getRequiredType().getSimpleName()
    );

    return new ErrorResponse(
        HttpStatus.BAD_REQUEST.toString(),
        List.of(new Error(message))
    );
  }

}
