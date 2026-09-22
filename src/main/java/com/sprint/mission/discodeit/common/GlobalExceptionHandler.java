package com.sprint.mission.discodeit.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(IllegalArgumentException e) {
        ApiError error = new ApiError("ILLEGAL_ARGUMENT", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }
    // NoSuchArguments -> NotFoundException
    @ExceptionHandler(BinaryContentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(BinaryContentNotFoundException e) {
        ApiError error = new ApiError("BINARY_CONTENT_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }
    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(ChannelNotFoundException e) {
        ApiError error = new ApiError("CHANNEL_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MessageNotFoundException e) {
        ApiError error = new ApiError("MESSAGE_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }
    @ExceptionHandler(ReadStatusNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(ReadStatusNotFoundException e) {
        ApiError error = new ApiError("READ_STATUS_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }
    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(UserNameNotFoundException e) {
        ApiError error = new ApiError("USERNAME_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(UserNotFoundException e) {
        ApiError error = new ApiError("USER_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }
    @ExceptionHandler(UserStatusNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(UserStatusNotFoundException e) {
        ApiError error = new ApiError("USER_STATUS_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }


}
