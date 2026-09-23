package com.sprint.mission.discodeit.dto.ChannelDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(

        UUID id,
        UUID ownerId,
        String channelName,
        String channelDescription,
        Instant lastMessageAt,
        List<UUID> userIds

) {
}