package com.sprint.mission.discodeit.dto.channel;

public record PublicChannelCreateRequest(
        String channelName,
        String description
) {
}
