package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class UserStatus extends Common {
    private UUID userId;
    private Instant lastOnlineAt;


    public UserStatus(User user) {
        super();
        this.userId = user.getId();
        this.lastOnlineAt = Instant.now();
    }

    public void updateOnlineAt() {
        this.lastOnlineAt = Instant.now();
    }

    public boolean isOnline() {
        Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        return !lastOnlineAt.isBefore(fiveMinutesAgo);
    }

}
