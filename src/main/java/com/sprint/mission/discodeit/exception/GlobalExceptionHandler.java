package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Spring MVC가 기본 예외를 등록하고 예외별 HTTP 상태와 헤더를 결정합니다.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleBase(DiscodeitException e) {

        ErrorCode errorCode = e.getErrorCode();
        log.warn("요청이 실패했습니다. code={}, message={}", errorCode.name(), errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(e));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.put(fe.getField(),fe.getDefaultMessage()));

        log.warn("입력값 검증 실패 {}", errors.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .toList());

        return handleExceptionInternal(e, ErrorResponse.of(ErrorCode.INVALID_INPUT, errors),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception e, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse response = body instanceof ErrorResponse errorResponse
                ? errorResponse
                : ErrorResponse.fromHttpStatus(status, e.getClass().getName());

        if (status.is5xxServerError()) {
            log.error("요청 처리 중 서버 오류가 발생했습니다. status={}", status.value(), e);
        }

        return super.handleExceptionInternal(e, response, headers, status, request);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        Map<String, Object> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        log.warn("입력값 검증 실패 {}", errors);

        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, errors, e.getClass().getName()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error("요청이 실패했습니다. 상정되지 않은 오류", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_ERROR,e.getClass().getName()));
    }
}
