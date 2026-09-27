package com.sprint.mission.discodeit.dto.Request;

import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID authorId,
        String content
) {
}
