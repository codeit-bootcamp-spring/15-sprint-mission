package com.sprint.mission.discodeit.dto.ChannelDto;

import java.util.UUID;

public record PublicChannelCreateRequest(

        UUID ownerId,
        String channelName,
        String channelDescription

) {
}