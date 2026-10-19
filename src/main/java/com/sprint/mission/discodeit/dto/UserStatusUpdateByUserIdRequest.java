package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateByUserIdRequest(

    @NotNull
    @Schema(description = "접속 상태를 수정할 사용자 ID")
    UUID userId,

    @NotNull
    @Schema(description = "변경할 마지막 활동 시각")
    Instant lastActiveAt) {

}
