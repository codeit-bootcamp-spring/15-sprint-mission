package com.sprint.mission.discodeit.exception;

import java.util.Map;

public abstract class StatusException extends DiscodeitException {

    protected StatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
