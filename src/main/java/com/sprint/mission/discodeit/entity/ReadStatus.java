package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;
@Getter
public class ReadStatus extends Common {
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;
    private Instant updatedAt = getCreatedAt();
    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = java.util.Objects.requireNonNull(lastReadAt);
    }
    public void update(Instant lastReadAt) {
        this.lastReadAt = java.util.Objects.requireNonNull(lastReadAt);
        updatedAt = Instant.now();
    }
}
