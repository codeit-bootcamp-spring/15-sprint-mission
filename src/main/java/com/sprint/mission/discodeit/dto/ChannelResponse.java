package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class ChannelResponse {
    private final UUID id;
    private final String type;
    private final String name;
    private final String description;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant lastMessageAt;
    private final List<UUID> participantIds;
    public ChannelResponse(UUID id, String type, String name, String description, Instant createdAt, Instant updatedAt, Instant lastMessageAt, List<UUID> participantIds) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastMessageAt = lastMessageAt;
        this.participantIds = List.copyOf(participantIds);
    }
}
