package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatusResponse {
    private final UUID id;
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    public ReadStatusResponse(UUID id, UUID userId, UUID channelId, Instant lastReadAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ReadStatusResponse from(ReadStatus readStatus){
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt()
        );
    }
}
