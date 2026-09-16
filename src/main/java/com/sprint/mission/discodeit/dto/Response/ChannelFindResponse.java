package com.sprint.mission.discodeit.dto.Response;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        ChannelType channelType,
        List<UUID> membersId,
        Instant lastMessageAt

) {
}
