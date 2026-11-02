package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {

}
