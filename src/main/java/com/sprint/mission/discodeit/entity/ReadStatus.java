package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements java.io.Serializable {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID channelId;
    private UUID userId;
    private Instant lastReadAt;

    public ReadStatus(UUID channelId, UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.channelId = channelId;
        this.userId = userId;
        this.lastReadAt = null;
    }

    public void updateLastReadAt(Instant lastReadAt) {

        if (lastReadAt == null) {
            throw new IllegalArgumentException("읽은 시간이 필요합니다.");
        }

        this.lastReadAt = lastReadAt;
        this.updatedAt = Instant.now();
    }
}
