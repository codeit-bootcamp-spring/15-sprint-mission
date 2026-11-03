package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "INVALID_ARGUMENT",
        e.getMessage(),
        Map.of(),
        e.getClass().getSimpleName(),
        400
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleException(NoSuchElementException e) {

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "RESOURCE_NOT_FOUND",
        e.getMessage(),
        Map.of(),
        e.getClass().getSimpleName(),
        404
    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "INTERNAL_SERVER_ERROR",
        "서버 내부 오류가 발생했습니다.",
        Map.of(),
        e.getClass().getSimpleName(),
        500
    );

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
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
