package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(

    @Schema(description = "접속 상태 ID")
    UUID id,

    @Schema(description = "사용자 ID")
    UUID userId,

    @Schema(description = "사용자의 마지막 활동 시각")
    Instant lastActiveAt) {

}