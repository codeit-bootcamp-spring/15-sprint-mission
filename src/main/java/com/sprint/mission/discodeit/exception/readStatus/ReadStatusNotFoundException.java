package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(UUID uuid, UUID channelId) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, Map.of("userId", uuid, "channelId", channelId));
  }

  public ReadStatusNotFoundException(UUID uuid) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, Map.of("readStatusId", uuid));
  }
}
