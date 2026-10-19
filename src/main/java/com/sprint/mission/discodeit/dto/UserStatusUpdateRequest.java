package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateRequest(

    @Schema(description = "수정할 접속 상태 ID")
    UUID id,

    @Schema(description = "요청의 마지막 활동 시각")
    Instant lastActiveAt) {

}
