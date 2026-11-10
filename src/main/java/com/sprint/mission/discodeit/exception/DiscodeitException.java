package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class DiscodeitException extends RuntimeException {

  private final ErrorCode errorCode;

  private final Instant timeStamp;

  private final Map<String, Object> details;


  protected DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.details = details;
    this.timeStamp = Instant.now();
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
