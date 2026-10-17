package com.sprint.mission.discodeit.dto;

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
    Instant lastMessageAt,
    List<UUID> participantUserIds
) {

}