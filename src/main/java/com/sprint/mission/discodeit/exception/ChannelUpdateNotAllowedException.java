package com.sprint.mission.discodeit.exception;

public class ChannelUpdateNotAllowedException extends ChannelException {

  public ChannelUpdateNotAllowedException() {
    super(ErrorCode.CHANNEL_UPDATE_NOT_ALLOWED);
  }
}