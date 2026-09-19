package com.sprint.mission.discodeit.common;

import java.util.UUID;

public class ReadStatusNotFoundException extends RuntimeException{
    public ReadStatusNotFoundException(UUID id) {
        super(id+"");

    }
}
