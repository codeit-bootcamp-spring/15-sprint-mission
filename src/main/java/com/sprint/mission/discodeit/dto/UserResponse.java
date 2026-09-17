package com.sprint.mission.discodeit.dto;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id, String nickname, String username, String email, Instant createdAt,
                           Instant updatedAt, UUID profileId, boolean online) {
}
