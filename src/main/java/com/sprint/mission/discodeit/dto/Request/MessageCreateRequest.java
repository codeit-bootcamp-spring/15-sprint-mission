package com.sprint.mission.discodeit.dto.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID userId,
        String messageString,
        Optional<List<UUID>> binaryIds
) {
}
