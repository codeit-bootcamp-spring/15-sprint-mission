package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
    return build(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", e, new HashMap<>());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));
    return build(HttpStatus.BAD_REQUEST, "VALID_EXCEPTION", e, details);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> noSuchElementHandler(NoSuchElementException e) {
    return build(HttpStatus.NOT_FOUND, "NO_CONTENTS", e, new HashMap<>());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", e, new HashMap<>());
  }

  private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, Exception e,
      Map<String, Object> details) {
    ErrorResponse body = new ErrorResponse(Instant.now(), status.value(), code, e.getMessage(),
        e.getClass().getSimpleName(), details);
    return ResponseEntity.status(status).body(body);
  }
}
