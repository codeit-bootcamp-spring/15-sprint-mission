package com.sprint.mission.discodeit.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(
        String name,
        String description
) {
}
