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

    // 마지막 접속 시간이 현재 시간으로부터 5분 이내 -> 현재 접속 중인 유저
    public boolean isOnline() {
        return lastAccessedAt != null &&
                lastAccessedAt.isAfter(Instant.now().minusSeconds(5*60));
    }
}
