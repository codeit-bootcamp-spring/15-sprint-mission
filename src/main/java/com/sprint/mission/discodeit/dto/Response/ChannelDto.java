package com.sprint.mission.discodeit.dto.Response;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String description,
        ChannelType type,
        List<UUID> participantIds,
        Instant lastMessageAt

) {

}
