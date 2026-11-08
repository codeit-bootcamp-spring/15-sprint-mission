package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> baseExceptionHandler(DiscodeitException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    log.warn("에러 발생 / 에러 코드 = {}, 에러 메세지 = {}", errorCode.getCode(), errorCode.getMessage());
    ErrorResponse body = new ErrorResponse(
        exception.getTimeStamp(),
        errorCode.getHttpStatus().value(),
        errorCode.getCode(),
        exception.getMessage(),
        exception.getClass().getSimpleName(),
        exception.getDetails()
    );
    return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ErrorResponse> missingServletRequestPart(
      MissingServletRequestPartException e) {
    return build(HttpStatus.BAD_REQUEST, "MISSING_REQUEST_PART", e, new HashMap<>());
  }

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
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("요청 처리 중 예상하지 못한 오류 발생", e);
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", e, new HashMap<>());
  }

  private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, Exception e,
      Map<String, Object> details) {
    ErrorResponse body = new ErrorResponse(Instant.now(), status.value(), code, e.getMessage(),
        e.getClass().getSimpleName(), details);
    return ResponseEntity.status(status).body(body);
  }
}
