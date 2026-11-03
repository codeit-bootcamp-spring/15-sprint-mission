package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class FileLoadFailedException extends DiscodeitException {


    public FileLoadFailedException(Throwable cause, UUID binaryContentId) {
        super(ErrorCode.FILE_LOAD_FAILED, Map.of("binaryContentId",binaryContentId));
        initCause(cause);
    }


}
