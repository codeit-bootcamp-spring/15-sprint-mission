package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity{

    private final UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt)
    {
        super();
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    // 서비스에서 호출하는 isOnline() 메서드
    public boolean isOnline() {
        if (this.lastActiveAt == null) {
            return false;
        }
        return this.lastActiveAt.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }

    public void update(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
        touch();
    }
}
