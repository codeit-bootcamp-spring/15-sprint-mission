package com.sprint.mission.discodeit.common;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(UUID id) {
        super(id + "");
    }
}
