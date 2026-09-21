package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;
import java.time.Instant;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
