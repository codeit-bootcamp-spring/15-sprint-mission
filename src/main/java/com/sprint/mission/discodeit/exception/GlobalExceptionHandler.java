package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleException(IllegalArgumentException e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(e.getMessage());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleException(NoSuchElementException e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(e.getMessage());
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(
      DiscodeitException e) {

    HttpStatus status = switch (e.getErrorCode()) {
      case USER_NOT_FOUND,
           CHANNEL_NOT_FOUND,
           MESSAGE_NOT_FOUND,
           BINARY_CONTENT_NOT_FOUND -> HttpStatus.NOT_FOUND;

      case USER_ALREADY_EXISTS -> HttpStatus.CONFLICT;

      case CHANNEL_UPDATE_NOT_ALLOWED -> HttpStatus.BAD_REQUEST;

      default -> HttpStatus.BAD_REQUEST;
    };

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        e.getErrorCode().getCode(),
        e.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        status.value()
    );

    return ResponseEntity.status(status).body(response);
  }
}
