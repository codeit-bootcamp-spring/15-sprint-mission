package com.sprint.mission.discodeit.dto.UserDto;

public record UserUpdateRequest(

        String name,
        String email,
        String password
) {
}
