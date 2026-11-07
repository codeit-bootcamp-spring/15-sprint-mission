package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;
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

}
