package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class DuplicateEmailException extends UserException {

  public DuplicateEmailException(String email) {
    super(ErrorCode.DUPLICATE_EMAIL, Map.of("email", email));
  }
}
