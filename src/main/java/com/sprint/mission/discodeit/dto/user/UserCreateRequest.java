package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}
