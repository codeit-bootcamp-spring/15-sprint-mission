package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageUpdateRequest(

    @NotNull
    @Schema(description = "수정 요청의 메시지 ID")
    UUID id,

    @Schema(description = "변경할 메시지 내용")
    @NotBlank
    String content) {

}
