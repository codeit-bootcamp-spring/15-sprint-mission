package com.sprint.mission.discodeit.common;

import java.util.UUID;

public class UserStatusNotFoundException extends RuntimeException {
    public UserStatusNotFoundException(UUID id) {
        super(id + "");
    }
}
