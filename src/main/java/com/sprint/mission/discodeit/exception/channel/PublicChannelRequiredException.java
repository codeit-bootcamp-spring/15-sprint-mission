package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class PublicChannelRequiredException extends ChannelException {

  public PublicChannelRequiredException(UUID channelId) {
    super(ErrorCode.PUBLIC_CHANNEL_REQUIRED, Map.of("channelId", channelId));
  }
}
