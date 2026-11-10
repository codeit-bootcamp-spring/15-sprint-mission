package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryNotFoundException extends BinaryException {

  public BinaryNotFoundException(UUID uuid) {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND, Map.of("binaryContentId", uuid));
  }
}
