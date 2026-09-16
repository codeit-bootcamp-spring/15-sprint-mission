package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String channelName,
        String description,
        String channelType,
        Instant lastMessageAt,
        List<UUID> participantIds,
        Instant createdAt,
        Instant updatedAt
) {
}