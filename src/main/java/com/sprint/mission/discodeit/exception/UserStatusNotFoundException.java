package com.sprint.mission.discodeit.exception;

import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends DiscodeitException {


    public UserStatusNotFoundException(UUID userId) {
        super(ErrorCode.USER_STATUS_NOT_FOUND, Map.of("userId",userId));
    }


}
