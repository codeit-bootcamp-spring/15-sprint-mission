package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChannelResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final ChannelType type;
    private final String name;
    //
    private final  String description;

    private final List<UUID> participantIds;

    private final Instant createdAt;

    public static ChannelResponse from(Channel channel) {
        return new ChannelResponse(channel.getId(), channel.getType(), channel.getName(), channel.getDescription(), channel.getParticipantIds(), channel.getCreatedAt());
    }

    public static ChannelResponse from(ChannelDto channelDto) {
        return new ChannelResponse(channelDto.id(), channelDto.type(), channelDto.name(), channelDto.description(), channelDto.participantIds(), channelDto.lastMessageAt());
    }
}
