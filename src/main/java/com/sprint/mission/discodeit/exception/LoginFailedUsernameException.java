package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class LoginFailedUsernameException extends DiscodeitException {


    public LoginFailedUsernameException() {
        super(ErrorCode.LOGIN_FAILED_USERNAME, null);
    }


}
