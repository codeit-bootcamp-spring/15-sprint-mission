package com.sprint.mission.discodeit.dto.Request;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId
) {
}
