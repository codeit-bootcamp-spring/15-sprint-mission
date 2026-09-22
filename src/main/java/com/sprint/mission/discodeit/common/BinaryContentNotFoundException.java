package com.sprint.mission.discodeit.common;

import java.util.UUID;

public class BinaryContentNotFoundException extends RuntimeException {
    public BinaryContentNotFoundException(UUID id) {
        super(id + "");
    }
}
