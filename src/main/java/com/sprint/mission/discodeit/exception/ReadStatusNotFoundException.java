package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends StatusException {


    public ReadStatusNotFoundException(UUID readStatusId) {
        super(ErrorCode.READ_STATUS_NOT_FOUND, Map.of("readStatusId",readStatusId));
    }


}
