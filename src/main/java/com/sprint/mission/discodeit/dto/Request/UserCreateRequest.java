package com.sprint.mission.discodeit.dto.Request;

import com.sprint.mission.discodeit.entity.NitroLevel;

import java.util.Optional;
import java.util.UUID;

public record UserCreateRequest(
        String email,
        String password,
        String name,
        NitroLevel nitroLevel,
        Optional<UUID> profileId
) {
}
