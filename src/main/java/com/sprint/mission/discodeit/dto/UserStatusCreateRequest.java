package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class UserStatusCreateRequest {
    private final UUID userId;
    private final Instant lastActiveAt;
    public UserStatusCreateRequest(UUID userId, Instant lastActiveAt) {
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }
}
