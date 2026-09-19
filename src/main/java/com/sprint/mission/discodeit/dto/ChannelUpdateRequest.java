package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelUpdateRequest(

        @NotNull
        UUID id,

        @NotBlank
        String name,

        String description) {

}
