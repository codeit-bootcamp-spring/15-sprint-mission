package com.sprint.mission.discodeit.dto.request;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequest(
        @NotNull(message = "채널명은 필수입니다.")
        UUID channelId,

        @NotNull(message = "보낸 사람은 필수입니다.")
        UUID authorId,

        String content
) {
}
