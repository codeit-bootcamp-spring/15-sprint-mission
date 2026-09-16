package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String userName,
        String email,
        UUID profileId,
        boolean online,
        Instant createdAt,
        Instant updatedAt
) {
}