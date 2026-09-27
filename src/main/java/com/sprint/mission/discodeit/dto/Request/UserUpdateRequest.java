package com.sprint.mission.discodeit.dto.Request;


import java.util.Optional;
import java.util.UUID;

public record UserUpdateRequest(
        String email,
        String password,
        String name,
        Optional<UUID> profileId
) {
}
