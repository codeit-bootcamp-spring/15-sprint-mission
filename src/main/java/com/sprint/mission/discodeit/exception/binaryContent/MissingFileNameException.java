package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MissingFileNameException extends BinaryException {

  public MissingFileNameException() {
    super(ErrorCode.MISSING_FILE_NAME, Map.of());
  }
}
