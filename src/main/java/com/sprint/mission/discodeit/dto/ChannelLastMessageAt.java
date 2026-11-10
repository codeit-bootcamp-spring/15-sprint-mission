package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record ChannelLastMessageAt(
    UUID channelId,
    Instant lastMessageAt
) {

}