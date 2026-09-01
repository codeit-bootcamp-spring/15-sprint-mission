package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class ReadStauts extends Common {
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStauts(User user, Channel channel) {
        super();
        this.userId = user.getId();
        this.channelId = channel.getId();
        this.lastReadAt = Instant.now();
    }
}
