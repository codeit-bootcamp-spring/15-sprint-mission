package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends Common {
    private final UUID userId;
    private Instant lastAccessedAt;

    public UserStatus(UUID userId, Instant lastAccessedAt) {
        this.userId = userId;
        this.lastAccessedAt = lastAccessedAt;
    }

    public void update(Instant lastAccessedAt) {
        if (lastAccessedAt != null) {
            this.lastAccessedAt = lastAccessedAt;
            updateUpdatedAt();
        }
    }
}