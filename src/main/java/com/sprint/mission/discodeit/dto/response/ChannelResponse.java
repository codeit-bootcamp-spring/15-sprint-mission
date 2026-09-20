package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelResponse {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String type;
    private final String name;
    private final String description;
    private final List<UUID> participantIds;
    private final Instant lastMessageAt;


    public ChannelResponse(UUID id, Instant createdAt, Instant updatedAt, String type, String name, String description, List<UUID> participantIds, Instant lastMessageAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.name = name;
        this.description = description;
        this.participantIds = participantIds;
        this.lastMessageAt = lastMessageAt;
    }

    public static ChannelResponse from(Channel channel){
        return new ChannelResponse(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getType().toString(),
                channel.getName(),
                channel.getDescription(),
                List.of(),
                Instant.MIN
        );
    }

    public static ChannelResponse from(ChannelDto channelDto){
        return new ChannelResponse(
                channelDto.id(),
                channelDto.createdAt(),
                null,
                channelDto.type().toString(),
                channelDto.name(),
                channelDto.description(),
                channelDto.participantIds(),
                channelDto.lastMessageAt()
        );
    }
}
