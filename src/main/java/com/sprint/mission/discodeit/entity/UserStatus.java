package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import java.time.*;
import java.util.UUID;
@Getter
public class UserStatus extends Common {
    private final UUID userId;
    private Instant lastActiveAt;
    private Instant updatedAt = getCreatedAt();
    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.userId = userId;
        this.lastActiveAt = java.util.Objects.requireNonNull(lastActiveAt);
    }
    public void update(Instant lastActiveAt) {
        this.lastActiveAt = java.util.Objects.requireNonNull(lastActiveAt);
        updatedAt = Instant.now();
    }
    public boolean isOnline() {
        Instant now = Instant.now();
        Duration elapsed = Duration.between(lastActiveAt, now);
        // Session decision: exactly five minutes is offline.
        return !elapsed.isNegative() && elapsed.compareTo(Duration.ofMinutes(5)) < 0;
    }
}
