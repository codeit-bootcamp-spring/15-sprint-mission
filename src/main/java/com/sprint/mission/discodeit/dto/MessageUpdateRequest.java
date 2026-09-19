package com.sprint.mission.discodeit.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageUpdateRequest(

        @NotNull
        UUID id,

        @NotBlank
        String content) {
}
