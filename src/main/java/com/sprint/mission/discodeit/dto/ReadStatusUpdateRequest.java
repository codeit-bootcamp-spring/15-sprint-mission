package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class ReadStatusUpdateRequest {
    private final UUID id;
    private final Instant lastReadAt;
    public ReadStatusUpdateRequest(UUID id, Instant lastReadAt) {
        this.id = id;
        this.lastReadAt = lastReadAt;
    }
}
