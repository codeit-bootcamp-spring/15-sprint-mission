package com.sprint.mission.discodeit.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(
        @NotBlank
        String name
) {
}
