package com.sprint.mission.discodeit.global;
public class ApiError {

    private final String code;
    private final String message;
    private final String exceptionClass;
    public ApiError(String code, String message, String exceptionClass) {
        this.code = code;
        this.message = message;
        this.exceptionClass=exceptionClass;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}