package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class BinaryRequestFailedException extends DiscodeitException {


    public BinaryRequestFailedException(Throwable cause, String fileName) {
        super(ErrorCode.BINARY_REQUEST_FAILED,
                fileName == null ? Map.of() : Map.of("fileName", fileName));
        initCause(cause);
    }


}
