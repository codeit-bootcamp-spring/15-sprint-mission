package com.sprint.mission.discodeit.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(

        @NotNull
        UUID id,

        Instant lastReadAt) {
}
