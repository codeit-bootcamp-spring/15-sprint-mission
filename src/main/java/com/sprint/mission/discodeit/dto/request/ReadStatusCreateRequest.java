package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
       @NotNull(message = "유저id는 필수입니다.")
        UUID userId,

        @NotNull(message = "채널id는 필수입니다.")
        UUID channelId,

        @NotNull(message = "마지막 읽기 시간은 필수입니다.")
        Instant lastReadAt//
) {
}
