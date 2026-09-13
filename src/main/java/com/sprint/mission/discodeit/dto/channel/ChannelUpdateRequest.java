package com.sprint.mission.discodeit.dto.channel;

public record ChannelUpdateRequest(
        String channelName,
        String description
) {
}
