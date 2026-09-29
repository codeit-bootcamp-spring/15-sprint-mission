package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String name,
        ChannelType type,
        List<UUID> participantIds,
        Instant lastMessageAt,
        Instant createdAt,
        Instant updatedAt
) {
}
