package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public interface ChannelLastMessageAt {
    UUID getChannelId();
    Instant getLatestMessageAt();
}