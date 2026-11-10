package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class NotChannelParticipantException extends ChannelException {

  public NotChannelParticipantException(UUID userId, UUID channelId) {
    super(ErrorCode.NOT_CHANNEL_PARTICIPANT, Map.of("userId", userId, "channelId", channelId));
  }

}
