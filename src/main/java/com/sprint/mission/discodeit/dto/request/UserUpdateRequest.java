package com.sprint.mission.discodeit.dto.request;



public record UserUpdateRequest(
        String newEmail,
        String newPassword,
        String newUsername
) {
}
