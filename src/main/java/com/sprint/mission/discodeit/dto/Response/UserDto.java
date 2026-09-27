package com.sprint.mission.discodeit.dto.Response;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String email,
        String username,
        Optional<UUID> profileId,
        boolean online
) {

}
