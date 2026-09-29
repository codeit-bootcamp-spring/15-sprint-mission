package com.sprint.mission.discodeit.dto.Request;


import java.util.Optional;
import java.util.UUID;

public record UserCreateRequest(
        String email,
        String password,
        String username

) {
}
