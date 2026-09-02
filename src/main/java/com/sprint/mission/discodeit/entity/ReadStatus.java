package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends Common {
    private final UUID userId; // 채널별로 1:N 관계.
    private final UUID channelId; // 사용자별로 1:N 관계 => 사용자 + 채널은 1대1 관계
    private Instant lastReadAt;


    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateReadAt() {
        this.lastReadAt = Instant.now();
        this.autoSetUpdatedAt();
    }
}
