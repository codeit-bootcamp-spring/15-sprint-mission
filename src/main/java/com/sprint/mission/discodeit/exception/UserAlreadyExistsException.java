package com.sprint.mission.discodeit.exception;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException() {
    super(ErrorCode.USER_ALREADY_EXISTS);
  }
}
