package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID userId;

    private Instant lastActiveAt;

    private final Instant createdAt;
    private Instant updatedAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void update(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
        this.updatedAt = Instant.now();
    }

    public boolean isOnline() {
        return Duration.between(lastActiveAt, Instant.now()).toMinutes() <= 5;
    }
}