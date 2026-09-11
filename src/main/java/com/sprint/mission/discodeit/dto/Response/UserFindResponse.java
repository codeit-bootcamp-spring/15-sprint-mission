package com.sprint.mission.discodeit.dto.Response;

import com.sprint.mission.discodeit.entity.NitroLevel;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record UserFindResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String email,
        String name,
        NitroLevel nitroLevel,
        Optional<UUID> profileId,
        boolean isOnline
) {

}
