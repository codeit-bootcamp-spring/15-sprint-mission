package com.sprint.mission.discodeit.dto.Request;



public record UserUpdateRequest(
        String newEmail,
        String newPassword,
        String newUsername
) {
}
