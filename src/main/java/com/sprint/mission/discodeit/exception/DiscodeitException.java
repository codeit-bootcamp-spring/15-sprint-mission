package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public abstract class DiscodeitException extends RuntimeException {
    private final Instant timestemp;
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    protected DiscodeitException(ErrorCode errorCode, Map<String, Object> details){
        this.timestemp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }



}
