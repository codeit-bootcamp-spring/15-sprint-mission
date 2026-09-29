package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDto (
        UUID id,
        String username,
        String email,
        UUID profileId,
        boolean online,
        Instant createdAt,
        Instant updatedAt
){
}
