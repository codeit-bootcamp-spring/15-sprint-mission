package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements java.io.Serializable {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    public void updateLastActiveAt() {
        Instant now = Instant.now();

        this.lastActiveAt = now;
        this.updatedAt = now;
    }

    public boolean isOnline() {
        if (this.lastActiveAt == null) {
            return false;
        }
        Instant now = Instant.now();

        // 현재 시간과 마지막 접속 시간 사이의 차이 계산
        Duration duration = Duration.between(this.lastActiveAt, now);

        // 시간 차이가 0분 이상이고 5분 이하인지 확인
        return !duration.isNegative()
                && duration.compareTo(Duration.ofMinutes(5)) <= 0;
    }

    public void updateLastActiveAt(Instant lastActiveAt) {

        if (lastActiveAt == null) {
            throw new IllegalArgumentException("마지막 활동 시간이 기록이 없습니다.");
        }

        this.lastActiveAt = lastActiveAt;
        this.updatedAt = Instant.now();
    }
}