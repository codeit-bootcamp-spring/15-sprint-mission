package com.sprint.mission.discodeit.common;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID id) {
        super(id+"");
    }
}
