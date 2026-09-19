package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(

        @NotBlank
        String name,

        String description) {
}
