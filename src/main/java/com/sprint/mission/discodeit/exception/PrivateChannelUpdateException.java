package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class PrivateChannelUpdateException extends DiscodeitException {


    public PrivateChannelUpdateException(UUID channelId) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE, Map.of("channelId",channelId));
    }


}
