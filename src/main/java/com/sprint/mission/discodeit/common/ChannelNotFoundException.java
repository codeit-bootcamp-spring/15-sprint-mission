package com.sprint.mission.discodeit.common;

import java.util.UUID;

public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException(UUID id) {
        super(id+ "");
    }
}
