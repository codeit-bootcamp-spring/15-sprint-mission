package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChannelUpdateRequest(

    @Size(max = 100)
    @Schema(description = "변경할 채널 이름")
    String name,

    @Size(max = 200)
    @Schema(description = "변경할 채널 소개")
    String description) {

}
