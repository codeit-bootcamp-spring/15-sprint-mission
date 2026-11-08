package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class PrivateChannelParticipantAddException extends ChannelException {

  public PrivateChannelParticipantAddException(UUID userId, UUID channelId) {
    super(ErrorCode.PRIVATE_CHANNEL_PARTICIPANT_ADD_NOT_ALLOWED,
        Map.of("userId", userId, "channelId", channelId));
  }
}
