package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class StorageInitFailedException extends BinaryContentException {


    public StorageInitFailedException(Throwable cause) {
        super(ErrorCode.STORAGE_INIT_FAILED, Map.of());
        initCause(cause);
    }


}
