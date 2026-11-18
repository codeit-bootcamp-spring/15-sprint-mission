package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class LoginFailedUsernameException extends UserException {


    public LoginFailedUsernameException() {
        super(ErrorCode.LOGIN_FAILED_USERNAME, Map.of());
    }


}
