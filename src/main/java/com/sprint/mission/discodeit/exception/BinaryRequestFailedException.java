package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class BinaryRequestFailedException extends DiscodeitException {


    public BinaryRequestFailedException(String fileName) {
        super(ErrorCode.BINARY_REQUEST_FAILED, Map.of("fileName",fileName));
    }


}
