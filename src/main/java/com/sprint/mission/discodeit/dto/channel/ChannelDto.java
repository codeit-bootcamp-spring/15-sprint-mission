package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    String type,
    String name,
    String description,
    List<UUID> participantsIds,
    Instant lastMessageAt
) {

}
