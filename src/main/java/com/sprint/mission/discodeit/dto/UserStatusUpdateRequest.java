package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class UserStatusUpdateRequest {
    private final UUID id;
    private final Instant lastActiveAt;
    public UserStatusUpdateRequest(UUID id, Instant lastActiveAt) {
        this.id = id;
        this.lastActiveAt = lastActiveAt;
    }
}
