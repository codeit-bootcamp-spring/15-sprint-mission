package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends DiscodeitException {



    public ChannelNotFoundException(UUID channelId) {

        super(ErrorCode.CHANNEL_NOT_FOUND, Map.of("channelId",channelId));
    }


}
