package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserProfileNotFoundException extends UserException {

  public UserProfileNotFoundException(UUID userId) {
    super(ErrorCode.USER_PROFILE_NOT_FOUND, Map.of("userId", userId));
  }
}
