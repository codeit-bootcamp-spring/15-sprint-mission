package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class UserStatus extends Common {
    @Getter
    private UUID userId;
    private Instant lastOnlineAt;


    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.lastOnlineAt = Instant.now();
    }

    public void updateOnlineAt() {
        this.lastOnlineAt = Instant.now();
        this.autoSetUpdatedAt();
    }

    public boolean isOnline() {
        Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        return !lastOnlineAt.isBefore(fiveMinutesAgo);
    }

}
