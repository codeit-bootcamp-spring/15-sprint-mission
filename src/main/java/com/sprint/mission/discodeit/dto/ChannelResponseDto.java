package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        String name,
        String description,
        ChannelType type,
        Instant lastMessageAt,
        List<UUID> participantUserIds,
        Instant createdAt,
        Instant updatedAt
) {
    public static ChannelResponseDto of(Channel channel, Instant lastMessageAt, List<UUID> participantUserIds) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                lastMessageAt,
                participantUserIds,
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}