package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelUpdateRequest(

    @NotNull
    @Schema(description = "수정 요청의 채널 ID")
    UUID id,

    @Schema(description = "변경할 채널 이름")
    @NotBlank
    String name,

    @Schema(description = "변경할 채널 소개")
    String description) {

}
