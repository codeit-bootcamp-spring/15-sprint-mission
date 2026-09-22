package com.sprint.mission.discodeit.entity;


import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class ReadStatus implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID id;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastReadAt;
    private UUID channelId;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
        this.lastReadAt = Instant.now();

    }
    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        this.updatedAt = Instant.now();
        return updatedAt;
    }

    public Instant getLastReadAt() {
        return lastReadAt;
    }

    public void updateLastReadAt() {
        this.lastReadAt = Instant.now();
        this.updatedAt = Instant.now();
    }

}
