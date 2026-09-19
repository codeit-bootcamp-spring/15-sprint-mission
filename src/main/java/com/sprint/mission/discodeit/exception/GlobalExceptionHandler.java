package com.sprint.mission.discodeit.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 요청
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    // 존재하지 않는 사용자
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(
            UserNotFoundException e
    ) {
        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }

    // 사용자 정보 중복
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException(
            DuplicateUserException e
    ) {
        return ResponseEntity
                .status(409)
                .body(e.getMessage());
    }

    // 존재하지 않는 채널
    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<String> handleChannelNotFoundException(
            ChannelNotFoundException e
    ) {
        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }

    // 채널 정보 중복
    @ExceptionHandler(DuplicateChannelException.class)
    public ResponseEntity<String> handleDuplicateChannelException(
            DuplicateChannelException e
    ) {
        return ResponseEntity
                .status(409)
                .body(e.getMessage());
    }

    // 존재하지 않는 메시지
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<String> handleMessageNotFoundException(
            MessageNotFoundException e
    ) {
        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }
}