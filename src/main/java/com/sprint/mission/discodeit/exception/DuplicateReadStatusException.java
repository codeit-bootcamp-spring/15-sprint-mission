package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class DuplicateReadStatusException extends DiscodeitException {


    public DuplicateReadStatusException(UUID channelId, UUID userId) {
        super(ErrorCode.DUPLICATE_READ_STATUS, Map.of("channelId",channelId,"userId",userId));
    }


}
