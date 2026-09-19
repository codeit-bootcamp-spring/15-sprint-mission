package com.sprint.mission.discodeit.dto;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateByUserIdRequest(
        UUID userId,
        Instant lastActiveAt) {
}
