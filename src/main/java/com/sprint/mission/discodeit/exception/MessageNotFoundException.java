package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends DiscodeitException {


    public MessageNotFoundException(UUID messageId) {
        super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("messageId",messageId));
    }


}
