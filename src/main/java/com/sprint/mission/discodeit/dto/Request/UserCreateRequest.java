package com.sprint.mission.discodeit.dto.Request;



public record UserCreateRequest(
        String email,
        String password,
        String username

) {
}
