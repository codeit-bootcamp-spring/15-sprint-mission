package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class LoginFailedPasswordException extends DiscodeitException {


    public LoginFailedPasswordException() {
        super(ErrorCode.LOGIN_FAILED_PASSWORD, Map.of());
    }


}
