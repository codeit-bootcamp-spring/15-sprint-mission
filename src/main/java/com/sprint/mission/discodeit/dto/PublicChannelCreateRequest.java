package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(

    @Size(max = 100)
    @NotBlank
    @Schema(description = "공개 채널 이름")
    String name,

    @Size(max = 200)
    @Schema(description = "공개 채널 소개")
    String description) {

}
