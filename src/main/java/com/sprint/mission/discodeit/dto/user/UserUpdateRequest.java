package com.sprint.mission.discodeit.dto.user;

public record UserUpdateRequest(
        String userName,
        String email,
        String password
) {
}
