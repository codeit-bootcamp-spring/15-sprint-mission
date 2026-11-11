package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class FileSaveFailedException extends DiscodeitException {


    public FileSaveFailedException(Throwable cause, UUID binaryContentId) {
        super(ErrorCode.FILE_SAVE_FAILED, Map.of("binaryContentId",binaryContentId));
        initCause(cause);
    }


}
