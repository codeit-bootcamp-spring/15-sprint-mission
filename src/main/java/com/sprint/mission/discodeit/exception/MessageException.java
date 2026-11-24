package com.sprint.mission.discodeit.exception;

import java.util.Map;

public abstract class MessageException extends DiscodeitException {

    protected MessageException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
