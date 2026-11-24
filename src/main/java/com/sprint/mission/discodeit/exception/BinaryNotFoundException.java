package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class BinaryNotFoundException extends BinaryContentException {


    public BinaryNotFoundException(UUID binaryContentId) {
        super(ErrorCode.BINARY_NOT_FOUND, Map.of("binaryContentId",binaryContentId));
    }


}
