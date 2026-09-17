package com.sprint.mission.discodeit.dto;
import java.util.UUID;

public record UserUpdateRequest(UUID id, String nickname, String username, String email, String password) {
}
