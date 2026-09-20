package com.sprint.mission.discodeit.dto.UserDto;

import java.time.Instant;
import java.util.UUID;

public record UserFindResponse(

        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {}