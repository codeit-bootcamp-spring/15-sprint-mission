package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class DuplicateUserEmailException extends DiscodeitException {


    public DuplicateUserEmailException(String email) {

        super(ErrorCode.DUPLICATE_USER_EMAIL, Map.of("Email",email));
    }


}
