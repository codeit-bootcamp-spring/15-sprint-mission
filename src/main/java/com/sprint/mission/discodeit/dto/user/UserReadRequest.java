package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserReadRequest(
        String username,
        String email,
        UUID profileId,
        boolean online
) {
}
