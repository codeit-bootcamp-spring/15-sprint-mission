package com.sprint.mission.discodeit.dto;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(
        UUID id,
        Instant lastActiveAt) {
}
