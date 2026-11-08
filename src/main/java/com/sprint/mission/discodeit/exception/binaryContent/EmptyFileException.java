package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class EmptyFileException extends BinaryException {

  public EmptyFileException() {
    super(ErrorCode.EMPTY_FILE, Map.of());
  }
}
