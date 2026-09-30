package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 원문 에러 기록을 위한 로거 선언
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. 커스텀 비즈니스 예외
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        ErrorResponse response = ErrorResponse.of(
                e.getStatus().value(),
                e.getStatus().getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(e.getStatus()).body(response);
    }

    // 2. 잘못된 인자 전달 시 (400 Bad Request) - 클라이언트 피드백을 위해 e.getMessage() 유지
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 3. 데이터를 찾지 못했을 때 (404 Not Found) - 클라이언트 피드백을 위해 e.getMessage() 유지
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 4. 기타 서버 내부 오류 (500 Internal Server Error) - e.getMessage() 제외 및 보안 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        // 서버 내부 스택/경로는 서버 로그에만 기록
        log.error("Unhandled server exception: ", e);

        // 클라이언트 응답에는 공격자 분석 방지용 고정 안내 문구만 전달
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}