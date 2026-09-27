package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(

    @NotNull
    @Schema(description = "읽음 상태를 생성할 사용자 ID")
    UUID userId,

    @NotNull
    @Schema(description = "읽음 상태를 관리할 채널 ID")
    UUID channelId,

    @Schema(description = "해당 채널의 메시지를 마지막으로 읽은 시각")
    Instant lastReadAt) {

}
