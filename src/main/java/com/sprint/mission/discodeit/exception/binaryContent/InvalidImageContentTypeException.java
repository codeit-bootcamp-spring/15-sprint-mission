package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidImageContentTypeException extends BinaryException {

  public InvalidImageContentTypeException(String extension, String contentType) {
    super(ErrorCode.INVALID_IMAGE_CONTENT_TYPE,
        Map.of("extension", extension, "contentType",
            contentType == null ? "unknown" : contentType));
  }
}
