package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class DuplicateUserNameException extends DiscodeitException {


    public DuplicateUserNameException(String userName) {
        super(ErrorCode.DUPLICATE_USER_NAME, Map.of("username",userName));
    }


}
