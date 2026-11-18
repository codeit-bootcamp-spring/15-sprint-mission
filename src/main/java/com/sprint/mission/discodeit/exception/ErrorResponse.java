package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private Instant timestamp;
    private String code;
    private String message;
    private Map<String, Object> details;
    private String exceptionType;
    private int status;

    //커스텀예외용
    public static ErrorResponse of(DiscodeitException e){
        ErrorCode errorCode = e.getErrorCode();
        return new ErrorResponse(e.getTimestamp(), errorCode.getCode(), errorCode.getMessage(), e.getDetails(),
                e.getClass().getName(), errorCode.getStatus().value());
    }

    public static ErrorResponse of(ErrorCode errorCode, Map<String, Object> details, String exceptionType) {
        return new ErrorResponse(Instant.now(), errorCode.getCode(), errorCode.getMessage(), details,
                exceptionType, errorCode.getStatus().value());
    }

    //valid용
    public static ErrorResponse of(ErrorCode errorCode,Map<String, Object> details){
        return new ErrorResponse(Instant.now(),errorCode.getCode(), errorCode.getMessage(),details,
                "MethodArgumentNotValidException",errorCode.getStatus().value());
    }


    //상정되지 않은 예외용
    public static ErrorResponse of(ErrorCode errorCode,String exceptionType){
        return new ErrorResponse(Instant.now(),errorCode.getCode(), errorCode.getMessage(),null,
                exceptionType,errorCode.getStatus().value());
    }

    //스프링 기본값 사용
    public static ErrorResponse fromHttpStatus(HttpStatusCode status, String exceptionType) {
        if (status.value() == HttpStatus.BAD_REQUEST.value()) {
            return of(ErrorCode.INVALID_INPUT, exceptionType);
        }
        if (status.is5xxServerError()) {
            return new ErrorResponse(Instant.now(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage(), null, exceptionType, status.value());
        }

        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        String message = httpStatus != null ? httpStatus.getReasonPhrase() : "요청을 처리할 수 없습니다.";
        return new ErrorResponse(Instant.now(), "HTTP_" + status.value(), message, null,
                exceptionType, status.value());
    }

}
