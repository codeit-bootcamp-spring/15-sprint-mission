package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(

    @NotBlank
    @Schema(description = "공개 채널 이름")
    String name,

    @Schema(description = "공개 채널 소개")
    String description) {

}
