package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(

    @NotNull
    @Schema(description = "수정 요청의 읽음 상태 ID")
    UUID id,

    @Schema(description = "요청의 마지막 읽음 시각")
    Instant lastReadAt) {

}
