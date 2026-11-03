package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class StorageInitFailedException extends DiscodeitException {


    public StorageInitFailedException() {
        super(ErrorCode.STORAGE_INIT_FAILED, null);
    }


}
