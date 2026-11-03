package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends DiscodeitException {


    public UserNotFoundException(UUID userId) {

        super(ErrorCode.USER_NOT_FOUND, Map.of("userId",userId));
    }


}
