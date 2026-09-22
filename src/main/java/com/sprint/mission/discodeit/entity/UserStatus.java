package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class UserStatus implements Serializable {

    private static final long serialVersionUID  = 1L;

    private UUID id;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastSeenAt;

    public UserStatus(UUID userid) {
        this.id =UUID.randomUUID();
        this.userId  = userId;
        this.createdAt =Instant.now();
        this.lastSeenAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        this.updatedAt = Instant.now();
        return updatedAt;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public boolean isOnline() {
        return lastSeenAt.isAfter(
                Instant.now().minusSeconds(300)
        );

    }


    public void updateLastSeenAt() {
        this.lastSeenAt = Instant.now();
    }
}
